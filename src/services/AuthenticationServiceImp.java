package services;

import core.HRMDAO;
import core.Human;

public class AuthenticationServiceImp implements AuthenticationService {
	@Override
	public boolean login(Human user) {
		// TODO Auto-generated method stub
		return HRMDAO.login(user);
	}
}
