package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validatorBean(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            batchInsertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            batchUpdateRoles(user);
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        HashMap<Integer, Set<Role>> mapRoles = jdbcTemplate.query("SELECT * FROM user_roles",
                new ResultSetExtractor<HashMap<Integer, Set<Role>>>() {
                    @Override
                    public HashMap<Integer, Set<Role>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        HashMap<Integer, Set<Role>> map = new HashMap<>();
                        while (rs.next()) {
                            map.computeIfAbsent(rs.getInt("user_id"), userId ->
                                    EnumSet.noneOf(Role.class)).add(Role.valueOf(rs.getString("role")));
                        }
                        return map;
                    }
                });
        for (User u : users) {
            u.setRoles(mapRoles.get(u.getId()));
        }
        return users;
    }

    public void batchInsertRoles(User u) {
        Set<Role> roles = u.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            this.jdbcTemplate.batchUpdate("INSERT into user_roles (role, user_id) values (?,?)", roles, roles.size(),
                    new ParameterizedPreparedStatementSetter<Role>() {
                @Override
                public void setValues(PreparedStatement ps, Role argument) throws SQLException {
                    ps.setString(1, argument.name());
                    ps.setInt(2, u.id());
                }
            });
        }
    }

    public void batchUpdateRoles(User u) {
        Set<Role> roles = u.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            this.jdbcTemplate.batchUpdate("update user_roles set role = ? where user_id = ?", roles, roles.size(),
                    new ParameterizedPreparedStatementSetter<Role>() {
                @Override
                public void setValues(PreparedStatement ps, Role argument) throws SQLException {
                    ps.setString(1, argument.name());
                    ps.setInt(2, u.id());
                }
            });
        }
    }

    public User setRoles(User u) {
        if (u != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, u.getId());
            u.setRoles(roles);
        }
        return u;
    }

    public void deleteRoles(User u) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", Role.class, u.getId());
    }
}
