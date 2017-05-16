package example.reamp.login;

import android.os.AsyncTask;

import etr.android.reamp.mvp.MvpPresenter;

public class LoginPresenter extends MvpPresenter<LoginState> {

    private final LoginService loginService;

    public LoginPresenter(LoginService loginService) {
        this.loginService = loginService;
    }

    public void login() {

        getStateModel().setShowProgress(true);
        getStateModel().setLoggedIn(null);
        sendStateModel();

        LoginTask loginTask = new LoginTask(getStateModel().getLogin(), getStateModel().getPassword()) {
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (error != null) {
                    getStateModel().errorAction(error.getMessage());
                }
                getStateModel().setLoggedIn(result);
                getStateModel().setShowProgress(false);
                sendStateModel();
            }
        };

        loginTask.execute();

    }

    public void loginChanged(String login) {
        getStateModel().setLogin(login);
        sendStateModel();
    }

    public void passwordChanged(String password) {
        getStateModel().setPassword(password);
        sendStateModel();
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        final String login;
        final String password;
        Throwable error;

        LoginTask(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return loginService.login(login, password);
            } catch (Throwable e) {
                error = e;
                return null;
            }
        }
    }
}