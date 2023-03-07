package com.rudra.speechmodulationapi.repository;

import com.rudra.speechmodulationapi.entity.UploadFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends CrudRepository<UploadFile,Long> {

}
