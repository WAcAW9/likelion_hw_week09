package com.wacaw.hw.domain.checklist.repository;

import com.wacaw.hw.domain.checklist.entity.ChecklistTemplates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistTemplateRepository
    extends JpaRepository<ChecklistTemplates, Long> {
}
