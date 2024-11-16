package me.bivhak.insurance.main.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "company_agent_permissions")
public class CompanyAgentPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ElementCollection(targetClass = EPermission.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "agent_permissions", joinColumns = @JoinColumn(name = "company_agent_permission_id"))
    @Column(name = "permission")
    private Set<EPermission> permissions;

    public CompanyAgentPermission() {
    }

    public CompanyAgentPermission(Company company, Agent agent, Set<EPermission> permissions) {
        this.company = company;
        this.agent = agent;
        this.permissions = permissions;
    }
}