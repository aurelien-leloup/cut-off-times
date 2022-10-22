package com.nordea.cutofftimes.daos;

import com.nordea.cutofftimes.entities.CutOffTime;
import org.springframework.data.repository.CrudRepository;

public interface CutOffTimeRepository extends CrudRepository<CutOffTime, String> {
}
