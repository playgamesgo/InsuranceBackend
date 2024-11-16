package me.bivhak.insurance.main.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "company_agent_permissions")
public class InsuranceAgentPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Insurance insurance;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ElementCollection(targetClass = EPermission.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "agent_permissions", joinColumns = @JoinColumn(name = "company_agent_permission_id"))
    @Column(name = "permission")
    private Set<EPermission> permissions;

    public InsuranceAgentPermission() {
    }

    public InsuranceAgentPermission(Insurance insurance, Agent agent, Set<EPermission> permissions) {
        this.insurance = insurance;
        this.agent = agent;
        this.permissions = permissions;
    }
}