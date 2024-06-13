package de.einsjustin.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.types.GsonRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffUtil {

  private static final List<UUID> staffMembers = new ArrayList<>();

  public static void fetchStaffMembers() {

    GsonRequest<JsonArray> request = Request.ofGson(JsonArray.class)
        .url("https://laby.net/api/badge/cbcf5a7c-d325-4c5e-b918-adbc98343195")
        .method(Method.GET)
        .async();

    request.execute(response -> {

      JsonArray jsonElements = response.get();
      for (JsonElement jsonElement : jsonElements) {
        staffMembers.add(UUID.fromString(jsonElement.getAsString()));
      }

    });
  }

  public static List<UUID> getStaffMembers() {
    return staffMembers;
  }
}
