package org.akvo.gae.remoteapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

public class NameEqualsCodeCleanup {

	static List<Entity> normalizeNameAndCode(final Entity e) {
		final List<Entity> changes = new ArrayList<Entity>();
		String name = (String) e.getProperty("name");
		String code = (String) e.getProperty("code");
		Date date = new Date();
		if (name == null || name.isEmpty()) {
			if (code == null || code.isEmpty()) {
				System.out.println("Check the name and code properties for "
						+ e.getKey());
			} else {
				e.setProperty("name", e.getProperty("code"));
				e.setProperty("lastUpdateDateTime", date);
				changes.add(e);

			}
		} else if (!name.equals(code)) {
			e.setProperty("code", e.getProperty("name"));
			e.setProperty("lastUpdateDateTime", date);
			changes.add(e);
		}
		return changes;
	}

	public static void main(String[] args) {

		final String instance = args[0];
		final String usr = args[1];
		final String pwd = args[2];

		List<Entity> changes = new ArrayList<Entity>();

		final RemoteApiOptions options = new RemoteApiOptions().server(
				instance, 443).credentials(usr, pwd);

		final RemoteApiInstaller installer = new RemoteApiInstaller();

		try {
			installer.install(options);
			final DatastoreService ds = DatastoreServiceFactory
					.getDatastoreService();
			final PreparedQuery surveyGroupQuery = ds.prepare(new Query(
					"SurveyGroup"));
			final PreparedQuery surveyQuery = ds.prepare(new Query("Survey"));

			for (Entity e : surveyGroupQuery.asIterable()) {
				changes.addAll(normalizeNameAndCode(e));
			}

			for (Entity e : surveyQuery.asIterable()) {
				changes.addAll(normalizeNameAndCode(e));
			}

			if (changes.isEmpty()) {
				System.out.println("No changes needed.");
			} else {
				ds.put(changes);
				System.out.println(String.format("Changed %s entities",
						changes.size()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			installer.uninstall();
		}
	}
}
