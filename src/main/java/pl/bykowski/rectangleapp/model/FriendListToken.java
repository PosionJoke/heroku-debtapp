package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendListToken implements Externalizable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private Long userId;
//    @ManyToMany(mappedBy = "invitesToFriendListSet", fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "invite_to_friend_list",
//            joinColumns = @JoinColumn(
//                    name = "debtorUser_id", referencedColumnName = "userId"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "inviting_user_id", referencedColumnName = "id"))
    @OneToMany(mappedBy = "invitesToFriendListSet", fetch = FetchType.LAZY)
    private Set<DebtorUser> usersWhoSendInvite = new HashSet<>();

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
