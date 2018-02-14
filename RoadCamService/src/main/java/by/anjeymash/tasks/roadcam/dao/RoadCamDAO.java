package by.anjeymash.tasks.roadcam.dao;

import java.util.List;

import by.anjeymash.tasks.roadcam.dao.exception.DAOException;
import by.anjeymash.tasks.roadcam.model.Registration;

public interface RoadCamDAO {
	public List<Registration> getRegistrations() throws DAOException;

	public Registration addRegistration(Registration reg) throws DAOException;

	public List<Registration> getRegistrationsForNum(String num) throws DAOException;

	public int getAllRegCount() throws DAOException;
}
