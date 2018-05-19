package org.allen.imocker.dao;

import org.allen.imocker.entity.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {

    ActivationCode findOneByActivateCode(String activationCode);
}
