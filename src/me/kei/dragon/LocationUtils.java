package me.kei.dragon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/*
 * Copyright (c) 2019 siloneco
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
/**
 *
 * さまざまな座標を簡単に取得できるUtilityクラス
 *
 * @author siloneco
 *
 */
public class LocationUtils {

    /**
     *
     * プレイヤーが向いている方向に、指定した距離間隔で座標を取得し返します
     *
     * @param p               対象プレイヤー
     * @param maxDistance     取得する最大距離
     * @param distanceBetween 座標との間の距離
     * @return 取得した座標のリスト
     * @throws IllegalArgumentException doubleの変数のいずれかが0以下、もしくは maxDistance <
     *                                  distanceBetween の場合
     */
    public static List<Location> getLookingLocations(Player p, double maxDistance, double distanceBetween) throws IllegalArgumentException {
        if ( maxDistance <= 0 || distanceBetween <= 0 || maxDistance < distanceBetween ) {
            throw new IllegalArgumentException();
        }

        List<Location> locations = new ArrayList<>();
        Location eye = p.getEyeLocation().clone();
        Vector v = adjustExactDistance(p.getEyeLocation().getDirection().clone().normalize(), distanceBetween);

        while ( p.getEyeLocation().distance(eye) <= maxDistance ) {
            locations.add(eye.clone());
            eye.add(v);
        }

        return locations;
    }

    /**
     * 指定した2つの座標間の点を、距離を指定してList形式で取得します
     *
     * @param loc1            線を構成する座標1
     * @param loc2            線を構成する座標2
     * @param distanceBetween 点と点の距離
     * @return 取得したLocationのList
     * @throws IllegalArgumentException 2つのワールドが違う、もしくは distanceBetween が0以下の場合
     */
    public static List<Location> getLocationsOnLine(Location loc1, Location loc2, double distanceBetween) throws IllegalArgumentException {
        if ( loc1.getWorld() != loc2.getWorld() || distanceBetween <= 0 ) {
            throw new IllegalArgumentException();
        }

        List<Location> locations = new ArrayList<>();
        loc1 = loc1.clone();
        loc2 = loc2.clone();

        Vector v = adjustExactDistance(loc2.clone().subtract(loc1.clone()).toVector().normalize(), distanceBetween);

        Location firstLocation1 = loc1.clone();
        while ( firstLocation1.distance(loc2) >= firstLocation1.distance(loc1) ) {
            locations.add(loc1.clone());
            loc1.add(v);
        }

        return locations;
    }

    /**
     * 2つの座標を角として、立体を形成する座標のリストを取得します
     *
     * @param loc1            立体を形成する角の座標1
     * @param loc2            立体を形成する角の座標2
     * @param distanceBetween 座標どうしの距離
     * @return 取得したLocationのList
     * @throws IllegalArgumentException 2つの座標のワールドが違う、もしくは distanceBetween が0以下
     */
    public static List<Location> getSquareLocations(Location loc1, Location loc2, double distanceBetween) throws IllegalArgumentException {
        if ( loc1.getWorld() != loc2.getWorld() || distanceBetween <= 0 ) {
            throw new IllegalArgumentException();
        }
        List<Location> locations = new ArrayList<>();

        List<Location> x = getLocationsOnLine(loc1, loc1.clone().add(loc2.getX() - loc1.getX(), 0, 0), distanceBetween);
        List<Location> y = getLocationsOnLine(loc1, loc1.clone().add(0, loc2.getY() - loc1.getY(), 0), distanceBetween);
        List<Location> z = getLocationsOnLine(loc1, loc1.clone().add(0, 0, loc2.getZ() - loc1.getZ()), distanceBetween);

        new ArrayList<Location>(x).forEach(loc -> {
            x.add(loc.clone().add(0, 0, loc2.getZ() - loc1.getZ()));
            x.add(loc.clone().add(0, loc2.getY() - loc1.getY(), 0));
            x.add(loc.clone().add(0, loc2.getY() - loc1.getY(), loc2.getZ() - loc1.getZ()));
        });

        new ArrayList<Location>(y).forEach(loc -> {
            y.add(loc.clone().add(0, 0, loc2.getZ() - loc1.getZ()));
            y.add(loc.clone().add(loc2.getX() - loc1.getX(), 0, 0));
            y.add(loc.clone().add(loc2.getX() - loc1.getX(), 0, loc2.getZ() - loc1.getZ()));
        });

        new ArrayList<Location>(z).forEach(loc -> {
            z.add(loc.clone().add(loc2.getX() - loc1.getX(), 0, 0));
            z.add(loc.clone().add(0, loc2.getY() - loc1.getY(), 0));
            z.add(loc.clone().add(loc2.getX() - loc1.getX(), loc2.getY() - loc1.getY(), 0));
        });

        Arrays.asList(x, y, z).forEach(locations::addAll);

        return locations;
    }

    /**
     * 渡されたベクトルを、指定したlengthに調整します
     *
     * @param v      対象ベクトル
     * @param length 調整したいlength
     * @return 同じベクトル
     */
    private static Vector adjustExactDistance(Vector v, double length) {
        return v.multiply(length / v.length());
    }
}