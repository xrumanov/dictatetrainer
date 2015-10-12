/**
 * Converting JSON to User entity and vice versa
 */
package cz.muni.fi.dictatetrainer.user.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class UserJsonConverter implements EntityJsonConverter<User> {

    @Override
    public User convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final User user = getUserInstance(jsonObject);
        user.setName(JsonReader.getStringOrNull(jsonObject, "name"));
        user.setEmail(JsonReader.getStringOrNull(jsonObject, "email"));
        user.setPassword(JsonReader.getStringOrNull(jsonObject, "password"));

        return user;
    }

    @Override
    public JsonElement convertToJsonElement(final User user) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("email", user.getEmail());
        jsonObject.addProperty("type", user.getUserType().toString());

        final JsonArray roles = new JsonArray();
        for (final User.Roles role : user.getRoles()) {
            roles.add(new JsonPrimitive(role.toString()));
        }
        jsonObject.add("roles", roles);
        jsonObject.addProperty("createdAt", DateUtils.formatDateTime(user.getCreatedAt()));

        return jsonObject;
    }

    public static String generateJWT(User user) {

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles().toString());
        Key apiKey = MacProvider.generateKey();


        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getEncoded().toString());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(user.getId().toString())
                .setIssuedAt(now)
                .setClaims(claims)
                .signWith(signatureAlgorithm, signingKey)
                .setHeaderParam("typ", "JWT");

        Long ttlMillis = 5 * 60 * 60000L;
        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    private User getUserInstance(final JsonObject userJson) {
        final User.UserType userType = User.UserType.valueOf(JsonReader.getStringOrNull(userJson, "type"));
        if (User.UserType.TEACHER.equals(userType)) {
            return new Teacher();
        }
        return new Student();
    }

}