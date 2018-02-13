package by.anjeymash.tasks.roadcam.service;

import java.util.List;
import by.anjeymash.tasks.roadcam.dao.exception.DAOException;
import by.anjeymash.tasks.roadcam.dao.impl.RoadCamDAOImpl;
import by.anjeymash.tasks.roadcam.model.Registration;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/registrations")
public class RoadCamService {
	private static final Logger log = LogManager.getRootLogger();
	private static final RoadCamDAOImpl roadCamDAO = new RoadCamDAOImpl();

	// URI:
	// /contextPath/servletPath/registrations
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Registration> getRegistrations() {
		List<Registration> list = null;
		try {
			list = (new RoadCamDAOImpl()).getRegistrations();
		} catch (DAOException e) {
			log.error("Exception in DAO", e);
		}
		return list;
	}

	// URI:
	// /contextPath/servletPath/registrations/{regNum}
	@GET
	@Path("/{regNum}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Registration> getRegistrationsForNum(@PathParam("regNum") String regNum) {
		List<Registration> list = null;
		try {
			list = (new RoadCamDAOImpl()).getRegistrationsForNum(regNum);
		} catch (DAOException e) {
			log.error("Exception in DAO", e);

		}
		return list;

	}

	// URI:
	// /contextPath/servletPath/registrations
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Registration addRegistration(Registration reg) {
		Registration registration = null;
		try {
			registration = roadCamDAO.addRegistration(reg);
		} catch (DAOException e) {
			log.error("Exception in DAO", e);
		}
		return registration;
	}

	// URI:
	// /contextPath/servletPath/registrations/stats/count
	@GET
	@Path("/stats/count")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getAllRegCount() {
		int count=0;
		try {
			count = roadCamDAO.getAllRegCount();
		} catch (DAOException e) {
			log.error("Exception in DAO", e);
		}
		return Integer.toString(count);
	}

}
