package vn.iotstar.repository;

import org.springframework.data.repository.CrudRepository;

import vn.iotstar.entity.ConfirmationToken;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
