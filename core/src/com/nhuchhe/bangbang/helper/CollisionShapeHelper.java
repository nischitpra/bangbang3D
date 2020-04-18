package com.nhuchhe.bangbang.helper;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.nhuchhe.bangbang.utilities.Constants;

import java.util.HashMap;
import java.util.Set;

public class CollisionShapeHelper {
    HashMap<String, btCollisionShape> collisionShapeMap = new HashMap<>();

    public btCollisionShape getPlayerShape() {
        return getSphereShape(Constants.CollisionObject.PLAYER, 0.2f);
    }

    public btCollisionShape getGrenadeShape() {
        return getSphereShape(Constants.CollisionObject.GRENADE, 0.2f);
    }

    public btCollisionShape getBulletShape() {
        return getSphereShape(Constants.CollisionObject.BULLET, 0.075f);
    }

    public btCollisionShape getGrenadeExplosionShape() {
        return getSphereShape(Constants.CollisionObject.GRENADE_EXPLOSION_SPHERE, 2);
    }

    public btCollisionShape getBulletExplosionShape() {
        return getSphereShape(Constants.CollisionObject.BULLET_EXPLOSION_SPHERE, 0.2f);
    }

    public btCollisionShape getTerrainShape(final Model model, final boolean optimize) {
        return getCompoundShape(Constants.CollisionObject.TERRAIN, model, optimize);
    }

    private btCollisionShape getSphereShape(String key, float radius) {
        btCollisionShape shape = collisionShapeMap.get(key);
        if (shape == null) {
            shape = new btSphereShape(radius);
            collisionShapeMap.put(key, shape);
        }
        return shape;
    }

    private btCollisionShape getCompoundShape(String key, final Model model, final boolean optimize) {
        btCollisionShape shape = collisionShapeMap.get(key);
        if (shape == null) {
            shape = getCompoundShape(model, optimize);
            collisionShapeMap.put(key, shape);
        }
        return shape;
    }

    public btCompoundShape getCompoundShape(final Model model, final boolean optimize) {
        btCompoundShape shape = new btCompoundShape();
        Array<Mesh> meshes = model.meshes;
        Array<Node> nodes = model.nodes;
        for (int i = meshes.size - 1; i >= 0; i--) {
            Mesh mesh = meshes.get(i);
            shape.addChildShape(nodes.get(i).localTransform, getConvexHullShape(mesh, optimize));
        }
        return shape;
    }

    private btConvexHullShape getConvexHullShape(final Mesh mesh, final boolean optimize) {
        final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;
        // now optimize the shape
        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        final btConvexHullShape result = new btConvexHullShape(hull);
        // delete the temporary shape
        shape.dispose();
        hull.dispose();
        return result;
    }

    public void dispose() {
        Set<String> keys = collisionShapeMap.keySet();
        for (String key : keys) {
            collisionShapeMap.get(key).dispose();
        }
    }

}