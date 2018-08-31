package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Universidad;

public interface SplashViewController {

    void Userloggedin();

    void userNotLoggedIn();

    void OnsuccesVerifyUniversity(Universidad mUniversidad);

    void OnFailedVerifyUniversity(String mensaje);

    void tryDownloadConfig();
}
