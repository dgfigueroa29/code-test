
package es.voghdev.prjdagger2.usecase;

import java.util.List;

import es.voghdev.prjdagger2.global.model.User;

public interface GetUsers {
    List<User> get();

    void getAsync(Listener listener);

    interface Listener {
        void onUsersReceived(List<User> users, boolean isCached);

        void onError(Exception e);

        void onNoInternetAvailable();
    }
}
