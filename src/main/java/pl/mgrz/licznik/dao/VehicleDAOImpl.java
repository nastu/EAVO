package pl.mgrz.licznik.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.mgrz.licznik.model.Refill;
import pl.mgrz.licznik.model.User;
import pl.mgrz.licznik.model.Vehicle;
import pl.mgrz.licznik.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Repository
public class VehicleDAOImpl implements VehicleDAO {

    @Autowired
    HttpSession session;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Vehicle> getVehiclesList() {
        List<Vehicle> vehicleList;
        Query query = getCurrentSession().createQuery("from Vehicle");
        vehicleList = query.list();

        return vehicleList;
    }

    public List<Vehicle> getVehicleListByUser(int id) {
        List<Vehicle> vehicleList;
        Query query = getCurrentSession().createQuery("SELECT v from User u INNER JOIN u.vehicles v WHERE u.id=:id");
        query.setParameter("id", id);
        vehicleList = query.list();

        return vehicleList;
    }

    public Vehicle getVehicle(int id) {
        Query query = getCurrentSession().createQuery("from Vehicle where id=:id");
        query.setParameter("id", id);
        Vehicle vehicle = (Vehicle) query.list().get(0);

        return vehicle;
    }

    public void addVehicle(Vehicle vehicle, int id) {
        User user = userService.getUser((String) session.getAttribute("username"));
        vehicle.setUser(user);
        Set<Refill> refills = null;
        vehicle.setRefill(refills);
        getCurrentSession().persist(vehicle);
    }

    public void removeVehicle(int id) {
        Vehicle vehicle = getVehicle(id);
        getCurrentSession().delete(vehicle);
    }

}