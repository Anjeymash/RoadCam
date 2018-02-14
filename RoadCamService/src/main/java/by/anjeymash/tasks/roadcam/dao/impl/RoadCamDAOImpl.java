package by.anjeymash.tasks.roadcam.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import by.anjeymash.tasks.roadcam.dao.RoadCamDAO;
import by.anjeymash.tasks.roadcam.dao.connection.ConnectionPool;
import by.anjeymash.tasks.roadcam.dao.exception.ConnectionPoolException;
import by.anjeymash.tasks.roadcam.dao.exception.DAOException;
import by.anjeymash.tasks.roadcam.model.Registration;

public class RoadCamDAOImpl implements RoadCamDAO {

	private static final int INDEX_ONE = 1;
	private static final int INDEX_TWO = 2;
	private static final int INDEX_THREE = 3;
	private static final String SELECT_REG = "SELECT * FROM REGISTRATION";
	private static final String SEL_BY_NUM = "SELECT * FROM registration WHERE (r_regnum=?)";
	private static final String INSERT_REG = "INSERT INTO REGISTRATION (r_id, r_regnum, r_regdate) VALUES (?, ?, ?)";
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ConnectionPool conPool = ConnectionPool.getInstance();
	private static final RoadCamDAOImpl instance = new RoadCamDAOImpl();

	public RoadCamDAOImpl() {
	}
	
	public static RoadCamDAOImpl getInstance() {
		return instance;
	}

	@Override
	public List<Registration> getRegistrations() throws DAOException {
		List<Registration> list = new ArrayList<Registration>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = conPool.takeConnection();
			con.setAutoCommit(false);
			try {
				st = con.createStatement();
				rs = st.executeQuery(SELECT_REG);

				while (rs.next()) {
					list.add(new Registration(rs.getLong(INDEX_ONE), rs.getString(INDEX_TWO),
							DATE_FORMATTER.format(rs.getTimestamp(INDEX_THREE))));
					con.commit();
				}

			} catch (SQLException e) {
				con.rollback();
			} finally {
				con.setAutoCommit(true);// close transaction
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException("SQL error", e);

		} finally {
			conPool.closeConnection(con, st, rs);
		}
		return list;

	}

	@Override
	public Registration addRegistration(Registration reg) throws DAOException {
		PreparedStatement ps = null;
		Connection con = null;
		Long id = null;
		java.util.Date currentDate = Calendar.getInstance().getTime();
		Timestamp time = new Timestamp(currentDate.getTime());
		try {
			con = conPool.takeConnection();
			ps = con.prepareStatement(INSERT_REG);
			ps.setString(INDEX_TWO, reg.getRegNum());
			ps.setTimestamp(INDEX_THREE, time);
			ps.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException("SQL error", e);
		} finally {
			conPool.closeConnection(con, ps);

		}
		reg.setRegId(id);
		reg.setRegDate(DATE_FORMATTER.format(currentDate));

		return reg;
	}

	@Override
	public List<Registration> getRegistrationsForNum(String num) throws DAOException {
		List<Registration> list = new ArrayList<Registration>();
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		try {
			con = conPool.takeConnection();
			con.setAutoCommit(false);// start transaction
			try {
				ps = con.prepareStatement(SEL_BY_NUM);
				ps.setString(INDEX_ONE, num);
				rs = ps.executeQuery();
				while (rs.next()) {
					list.add(new Registration(rs.getLong(INDEX_ONE), rs.getString(INDEX_TWO),
							DATE_FORMATTER.format(rs.getTimestamp(INDEX_THREE))));
					con.commit();
				}
				con.commit();
			} catch (SQLException e) {
				con.rollback();
			} finally {
				con.setAutoCommit(true);// close transaction
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException("SQL error", e);

		} finally {
			conPool.closeConnection(con, ps, rs);
		}
		return list;
	}

	@Override
	public int getAllRegCount() throws DAOException {
		Statement st = null;
		Connection con = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = conPool.takeConnection();
			st = con.createStatement();
			rs = st.executeQuery(SELECT_REG);
			while (rs.next()) {
				count++;
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException("SQL error", e);
		} finally {
			conPool.closeConnection(con, st, rs);
		}
		return count;

	}
}
