package services;

import core.Human;
import core.HumanDAO;

public class AuthenticationServiceImp implements AuthenticationService {
	@Override
	public boolean login(Human user) {
		// TODO Auto-generated method stub
		return HumanDAO.login(user);
	}
}
