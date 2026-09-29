package offer_tracker.repository;

import offer_tracker.entity.ApplicationStatus;
import offer_tracker.entity.JobApplication;
import org.springframework.data.jpa.domain.Specification;

public final class JobApplicationSpecifications {

    private JobApplicationSpecifications() {
    }

    public static Specification<JobApplication> belongsTo(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<JobApplication> hasStatus(ApplicationStatus status) {
        if (status == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<JobApplication> companyContains(String company) {
        if (company == null || company.isBlank()) {
            return Specification.unrestricted();
        }
        String pattern = "%" + company.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.<String>get("company")), pattern);
    }
}