package midi.common.security;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "table_user_roles", uniqueConstraints = @UniqueConstraint(columnNames = { "role", "username" }))
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id", unique = true, nullable = false)
    private Long userRoleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;
    @Column(name = "role", nullable = false, length = 45)
    private String role;

    public UserRole() {
    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public Long getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
