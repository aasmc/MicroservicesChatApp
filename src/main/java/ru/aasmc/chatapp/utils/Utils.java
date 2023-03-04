package ru.aasmc.chatapp.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import ru.aasmc.chatapp.dto.User;

@UtilityClass
public class Utils {

    public static String chatId(long user1Id, long user2Id) {
        long minId = Math.min(user1Id, user2Id);
        long maxId = Math.max(user1Id, user2Id);

        return minId + "_" + maxId;
    }

    public static boolean isChatMember(long userId, String chatId) {
        String[] members = chatId.split("_");

        return Long.parseLong(members[0]) == userId || Long.parseLong(members[1]) == userId;
    }

    public static User user(Object obj) {
        if (obj instanceof User) {
            return (User) obj;
        }

        if (obj instanceof UsernamePasswordAuthenticationToken) {
            return (User) ((UsernamePasswordAuthenticationToken) obj).getPrincipal();
        }
        if (obj instanceof SecurityContext) {
            var ctx = (SecurityContext) obj;
            if (ctx.getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                var token = (UsernamePasswordAuthenticationToken) ctx.getAuthentication();
                return (User) token.getPrincipal();
            }
        }
        return null;
    }
}
