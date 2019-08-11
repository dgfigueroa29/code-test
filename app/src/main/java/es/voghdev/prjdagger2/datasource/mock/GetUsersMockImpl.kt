package es.voghdev.prjdagger2.datasource.mock

import java.util.ArrayList
import java.util.Random
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.GetUsers

class GetUsersMockImpl : GetUsers {
    private fun generateMockUser(id: String, name: String, addr: String): User {
        val u = User()
        u.id = id
        u.name = name
        u.address = addr
        return u
    }

    override fun get(): List<User> {
        return generateMockedUsers()
    }

    override fun getAsync(listener: GetUsers.Listener?) {
        val users = generateMockedUsers()
        val random = Random().nextInt(10)
        when {
            random < 8 -> listener!!.onUsersReceived(users, true)
            random in 9..9 -> listener!!.onNoInternetAvailable()
            else -> listener!!.onError(Exception("Unparseable response"))
        }
    }

    private fun generateMockedUsers(): List<User> {
        val users = ArrayList<User>()
        users.add(generateMockUser("1", "Antonio", "I.E.S. Zaidin-Vergeles, 5"))
        users.add(generateMockUser("2", "Juan", "I.E.S. Zaidin-Vergeles, 6"))
        users.add(generateMockUser("3", "Ana", "I.E.S. Zaidin-Vergeles, 7"))
        users.add(generateMockUser("4", "Isabel", "I.E.S. Zaidin-Vergeles, 8"))
        return users
    }
}
