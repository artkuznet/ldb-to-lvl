package com.artkuznet.converter.util;

import com.artkuznet.converter.maxed2.entity.Entity;

import java.util.*;
import java.util.stream.Collectors;

public class EntityTreeBuilder {

    public static List<Entity> buildEntityTree(List<Entity> entities, Map<Entity, Entity> entityParentMap) {
        Set<Entity> allEntities = new LinkedHashSet<>();
        allEntities.addAll(entities);
        allEntities.addAll(entityParentMap.keySet());

        Map<Entity, Entity> currentParents = new HashMap<>();
        for (Entity entity : allEntities) {
            currentParents.put(entity, entity.getParentEntity());
        }

        for (Entity entity : allEntities) {
            if (entity.getParentEntity() == null) {
                entity.setParentEntity(entityParentMap.get(entity));
            }
        }

        for (Entity entity : allEntities) {
            Entity previousParent = currentParents.get(entity);
            Entity newParent = entity.getParentEntity();

            if (previousParent != newParent) {
                if (previousParent != null) {
                    previousParent.getChildEntities().remove(entity);
                }
            }
        }

        Map<Entity, Set<Entity>> childrenToAddMap = new HashMap<>();
        for (Entity entity : allEntities) {
            Entity parent = entity.getParentEntity();
            if (parent != null) {
                childrenToAddMap.computeIfAbsent(parent, k -> new HashSet<>()).add(entity);
            }
        }

        for (Map.Entry<Entity, Set<Entity>> entry : childrenToAddMap.entrySet()) {
            Entity parent = entry.getKey();
            Set<Entity> newChildren = entry.getValue();

            Set<Entity> existingChildren = new HashSet<>(parent.getChildEntities());
            for (Entity child : newChildren) {
                if (!existingChildren.contains(child)) {
                    parent.addChildEntity(child);
                }
            }
        }

        List<Entity> roots = new ArrayList<>();
        for (Entity entity : allEntities) {
            if (entity.getParentEntity() == null) {
                roots.add(entity);
            }
        }

        List<Entity> result = new ArrayList<>();
        Queue<Entity> queue = new LinkedList<>(roots);

        while (!queue.isEmpty()) {
            Entity current = queue.poll();
            result.add(current);

            queue.addAll(current.getChildEntities());
        }

        return result.stream()
                .filter(e -> e.getParentEntity() == null)
                .collect(Collectors.toList());
    }
}
