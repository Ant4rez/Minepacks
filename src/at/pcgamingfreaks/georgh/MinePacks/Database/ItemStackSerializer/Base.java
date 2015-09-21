/*
 *   Copyright (C) 2014-2015 GeorgH93
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

package at.pcgamingfreaks.georgh.MinePacks.Database.ItemStackSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Base
{
	public byte[] toByteArray(Inventory inv)
	{
		byte[] ba = null;
		try
		{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    BukkitObjectOutputStream output = new BukkitObjectOutputStream(b);
		    output.writeObject(inv.getContents());
		    output.close();
		    ba = b.toByteArray();
		    output.close();
		    return ba;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ba;
	}
	
	public ItemStack[] toItemStack(byte[] data)
	{
		if(data != null)
		{
			try
			{
				BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(new ByteArrayInputStream(data));
				ItemStack[] its = (ItemStack[]) bukkitObjectInputStream.readObject();
				bukkitObjectInputStream.close();
				return its;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}