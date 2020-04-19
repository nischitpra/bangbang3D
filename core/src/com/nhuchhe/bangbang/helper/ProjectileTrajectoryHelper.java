package com.nhuchhe.bangbang.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector3;
import com.nhuchhe.bangbang.BangBang;

public class ProjectileTrajectoryHelper {
    ModelBuilder modelBuilder;
    MeshPartBuilder builder;
    Bezier<Vector3> path;
    Material material;

    final int TRAJECTORY_POINTS_COUNT = 100;
    final int BEZIER_POINTS_COUNT = 3;
    Vector3[] points = new Vector3[BEZIER_POINTS_COUNT];

    ModelInstance trajectoryInstance;

    public ProjectileTrajectoryHelper() {
        modelBuilder = new ModelBuilder();
        material = new Material();
        path = new Bezier<>();
        for (int i = BEZIER_POINTS_COUNT - 1; i >= 0; i--) {
            points[i] = new Vector3();
        }
    }

    public void setTrajectory(Vector3 forceDirection, Vector3 startPosition) {
        startPosition.y += 2;
        // need to calculate 3 points, start point, end point and peak height point and calculate time duration to reach ground.
        for (int i = 0; i < BEZIER_POINTS_COUNT; i++) {
            int time = i * 100;
            float theta = (float) Math.atan2(-forceDirection.y, forceDirection.x);
            float x = startPosition.x + (float) (forceDirection.x * time * Math.cos(theta));
            float y = startPosition.y + (float) (forceDirection.y * time * Math.sin(theta) - 0.5 * BangBang.world.getGravity().y * time * time);
            points[i].x = x;
            points[i].y = y;
//            points[i].z = z;
        }
        path.set(points);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, material);
        builder.setColor(Color.RED);
        buildTrajectoryModel();
        Model lineModel = modelBuilder.end();
        trajectoryInstance = new ModelInstance(lineModel);    // todo: need to dispose old lineInstance. This is not good at all. Need to find a wayt to reuse the instance.
        /**
         * todo:
         * Shaperenderer can be used to draw 3d lines.. according to https://badlogicgames.com/forum/viewtopic.php?f=11&t=14136
         * If so,, use that.
         */
    }

    private void buildTrajectoryModel() {
        for (int i = 0; i < TRAJECTORY_POINTS_COUNT; i++) {
            float t = i / TRAJECTORY_POINTS_COUNT * 1.0f;
            Vector3 st = new Vector3();
            Vector3 end = new Vector3();
            path.valueAt(st, t);
            path.valueAt(end, t - 0.01f);
            builder.line(st, end);
        }
    }

    public void drawTrajectory() {
        BangBang.modelBatch.render(trajectoryInstance);
    }
}
