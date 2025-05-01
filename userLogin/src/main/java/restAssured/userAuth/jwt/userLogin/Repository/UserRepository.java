package restAssured.userAuth.jwt.userLogin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import restAssured.userAuth.jwt.userLogin.Entity.User;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Integer> {

   User findByEmail(String email) throws Exception;

    User findById(String userId) throws Exception;
    User save(User user);
}
