package me.bivhak.insurance.main.repository;

import java.util.Optional;

import me.bivhak.insurance.main.models.Agent;
import me.bivhak.insurance.main.models.Company;
import me.bivhak.insurance.main.models.RefreshToken;
import me.bivhak.insurance.main.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  void deleteByUser(User user);

  @Modifying
  void deleteByAgent(Agent agent);

  @Modifying
  void deleteByCompany(Company company);
}
