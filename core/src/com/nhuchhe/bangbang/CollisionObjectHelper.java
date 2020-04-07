package com.nhuchhe.bangbang;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.utils.Array;

public class CollisionObjectHelper {
    public static btCompoundShape getCompoundShape(final Model model, final boolean optimize) {
        btCompoundShape shape = new btCompoundShape();
        Array<Mesh> meshes = model.meshes;
        Array<Node> nodes = model.nodes;
        for (int i = meshes.size - 1; i >= 0; i--) {
            Mesh mesh = meshes.get(i);
            shape.addChildShape(nodes.get(i).localTransform, getConvexHullShape(mesh, optimize));
        }
        return shape;
    }

    public static btConvexHullShape getConvexHullShape(final Model model, final boolean optimize) {
        final Mesh mesh = model.meshes.get(0);
        return getConvexHullShape(mesh, optimize);
    }

    private static btConvexHullShape getConvexHullShape(final Mesh mesh, final boolean optimize) {
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
}
