package in.co.online.food.delivery.model;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.online.food.delivery.bean.DessertBean;
import in.co.online.food.delivery.bean.DessertOrderBean;
import in.co.online.food.delivery.bean.UserBean;
import in.co.online.food.delivery.exception.ApplicationException;
import in.co.online.food.delivery.exception.DatabaseException;
import in.co.online.food.delivery.exception.DuplicateRecordException;
import in.co.online.food.delivery.util.DataUtility;
import in.co.online.food.delivery.util.JDBCDataSource;



public class DessertOrderModel {
	
	private static Logger log = Logger.getLogger(DessertOrderModel.class);

	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM OF_DessertORDER");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	public Integer nextOrderId() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ORDERID) FROM OF_DessertORDER");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}
	
	
	public DessertOrderBean findByPk(long pk) throws ApplicationException {
		log.debug("Model findByLogin Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM OF_DessertORDER WHERE ID=?");
		DessertOrderBean bean = null;
		Connection conn = null;
		System.out.println("sql" + sql);

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DessertOrderBean();
				bean.setId(rs.getLong(1));
				bean.setUserId(rs.getLong(2));
				bean.setUserName(rs.getString(3));
				bean.setDessertId(rs.getLong(4));
				bean.setDessertName(rs.getString(5));
				bean.setOrderId(rs.getLong(6));
				bean.setCity(rs.getString(7));
				bean.setPinCode(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setDateTime(rs.getTimestamp(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));
			

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting User by login");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByLogin End");
		return bean;
	}
	
	
	public long add(DessertOrderBean bean) throws ApplicationException, DuplicateRecordException, FileNotFoundException {

		Connection conn = null;
		int pk = 0;

		

		UserModel model = new UserModel();
		UserBean uBean = model.findByPK(bean.getUserId());
		
		DessertModel rmodel = new DessertModel();
		DessertBean rBean = rmodel.findByPk(bean.getDessertId());


		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			// Get auto-generated next primary key
			System.out.println(pk + " in ModelJDBC");
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn
					.prepareStatement("INSERT INTO OF_DessertORDER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setLong(2, bean.getUserId());
			pstmt.setString(3, uBean.getFirstName()+" "+uBean.getLastName());
			pstmt.setLong(4, bean.getDessertId());
			pstmt.setString(5, rBean.getDessertName());
			pstmt.setLong(6, nextOrderId());
			pstmt.setString(7, bean.getCity());
			pstmt.setString(8, bean.getPinCode());
			pstmt.setString(9, bean.getAddress());
			pstmt.setTimestamp(10 ,DataUtility.getCurrentTimestamp());
			pstmt.setString(11, bean.getCreatedBy());
			pstmt.setString(12, bean.getModifiedBy());
			pstmt.setTimestamp(13, bean.getCreatedDatetime());
			pstmt.setTimestamp(14, bean.getModifiedDatetime());
		
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}
	
	
	public void delete(DessertOrderBean bean) throws ApplicationException {

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM OF_DessertORDER WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}
	
	
	public List search(DessertOrderBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}


	public List search(DessertOrderBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM OF_DessertORDER WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getDessertName() != null && bean.getDessertName().length() > 0) {
				sql.append(" AND DessertName like '" + bean.getDessertName() + "%'");
			}
			if (bean.getUserName() != null && bean.getUserName().length() > 0) {
				sql.append(" AND UserName like '" + bean.getUserName() + "%'");
			}
			
			if (bean.getDessertId() > 0) {
				sql.append(" AND DessertId = " + bean.getDessertId());
			}
			
			if (bean.getUserId() > 0) {
				sql.append(" AND UserId = " + bean.getUserId());
			}
			
			
			

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
		}

		System.out.println("user model search  :"+sql);
		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DessertOrderBean();
				bean.setId(rs.getLong(1));
				bean.setUserId(rs.getLong(2));
				bean.setUserName(rs.getString(3));
				bean.setDessertId(rs.getLong(4));
				bean.setDessertName(rs.getString(5));
				bean.setOrderId(rs.getLong(6));
				bean.setCity(rs.getString(7));
				bean.setPinCode(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setDateTime(rs.getTimestamp(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;
	}
	
	public List list() throws ApplicationException {
		return list(0, 0);
	}
	
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from OF_DessertORDER");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		
		System.out.println("sql in list user :"+sql);
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DessertOrderBean bean = new DessertOrderBean();
				bean.setId(rs.getLong(1));
				bean.setUserId(rs.getLong(2));
				bean.setUserName(rs.getString(3));
				bean.setDessertId(rs.getLong(4));
				bean.setDessertName(rs.getString(5));
				bean.setOrderId(rs.getLong(6));
				bean.setCity(rs.getString(7));
				bean.setPinCode(rs.getString(8));
				bean.setAddress(rs.getString(9));
				bean.setDateTime(rs.getTimestamp(10));
				bean.setCreatedBy(rs.getString(11));
				bean.setModifiedBy(rs.getString(12));
				bean.setCreatedDatetime(rs.getTimestamp(13));
				bean.setModifiedDatetime(rs.getTimestamp(14));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}

}
