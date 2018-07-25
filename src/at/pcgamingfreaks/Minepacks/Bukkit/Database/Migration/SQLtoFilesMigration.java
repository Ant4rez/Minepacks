/*
 *   Copyright (C) 2018 GeorgH93
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.pcgamingfreaks.Minepacks.Bukkit.Database.Migration;

import at.pcgamingfreaks.Minepacks.Bukkit.Database.Database;
import at.pcgamingfreaks.Minepacks.Bukkit.Database.SQL;
import at.pcgamingfreaks.Minepacks.Bukkit.Minepacks;
import at.pcgamingfreaks.Reflection;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLtoFilesMigration extends Migration
{
	private final String sqlQuery;
	private final File saveFolder;

	protected SQLtoFilesMigration(@NotNull Minepacks plugin, @NotNull Database oldDb) throws InvocationTargetException, IllegalAccessException
	{
		super(plugin, oldDb);
		@Language("SQL") String query = "SELECT " + (plugin.getConfiguration().getUseUUIDs() ? "{FieldUUID}" : "{FieldName}") + ",{FieldBPITS},{FieldBPVersion} FROM {TablePlayers} INNER JOIN {TableBackpacks} ON {FieldPlayerID}={FieldBPOwner};";
		sqlQuery = (String) Reflection.getMethod(SQL.class, "replacePlaceholders", String.class).invoke(plugin.getDatabase(), query);
		saveFolder = new File(this.plugin.getDataFolder(), "backpacks");
		if(!saveFolder.exists() && !saveFolder.mkdirs())
		{
			plugin.getLogger().warning("Failed to create save folder (" + saveFolder.getAbsolutePath() + ").");
		}
	}

	@Override
	public @Nullable MigrationResult migrate() throws Exception
	{
		int migrated = 0;
		try(Connection connection = (Connection) Reflection.getMethod(SQL.class, "getConnection").invoke(plugin.getDatabase()); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sqlQuery))
		{
			while(rs.next())
			{
				try(FileOutputStream fos = new FileOutputStream(new File(saveFolder, rs.getString(1) + ".backpack")))
				{
					fos.write(rs.getInt(3));
					fos.write(rs.getBytes(2));
				}
				migrated++;
			}
		}
		return new MigrationResult("Migrated " + migrated + " backpacks from SQL to files.", MigrationResult.MigrationResultType.SUCCESS);
	}
}