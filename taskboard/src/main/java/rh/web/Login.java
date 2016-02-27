package rh.web;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class Login {

    @Autowired
    private GoogleIdTokenVerifier verifier;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public void login(
        @RequestParam("tokenId") String tokenId,
        HttpSession session) throws GeneralSecurityException, IOException {

        if (tokenId != null) {

            GoogleIdToken googleIdToken = verifier.verify(tokenId);

            if (googleIdToken != null) {

                session.setAttribute("tokenId", tokenId);
                session.setAttribute("user", googleIdToken.getPayload().getEmail());
            }

        }
    }
}
