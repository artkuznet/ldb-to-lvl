package com.artkuznet.converter.ldb;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.ldb.animation.Animation;
import com.artkuznet.converter.ldb.animation.AnimationContainer;
import com.artkuznet.converter.ldb.animation.Graph;
import com.artkuznet.converter.ldb.bsp.BSPNode;
import com.artkuznet.converter.ldb.bsp.BSPPolygon;
import com.artkuznet.converter.ldb.bsp.BSPPolygonIndex;
import com.artkuznet.converter.ldb.bsp.BSPVertex;
import com.artkuznet.converter.ldb.character.Character;
import com.artkuznet.converter.ldb.dynamiclight.DynamicLight;
import com.artkuznet.converter.ldb.dynamicmesh.LdbDynamicMesh;
import com.artkuznet.converter.ldb.dynamicmesh.DynamicMeshConfig;
import com.artkuznet.converter.ldb.exit.Exit;
import com.artkuznet.converter.ldb.fsm.*;
import com.artkuznet.converter.ldb.item.Item;
import com.artkuznet.converter.ldb.lightmap.LightmapTexture;
import com.artkuznet.converter.ldb.material.Material;
import com.artkuznet.converter.ldb.material.MaterialContainer;
import com.artkuznet.converter.ldb.pointlight.PointLight;
import com.artkuznet.converter.ldb.polygon.Polygon;
import com.artkuznet.converter.ldb.polygon.PolygonContainer;
import com.artkuznet.converter.ldb.property.EntityProperties;
import com.artkuznet.converter.ldb.room.Room;
import com.artkuznet.converter.ldb.staticmesh.StaticMesh;
import com.artkuznet.converter.ldb.texture.Texture;
import com.artkuznet.converter.ldb.texture.TextureVertex;
import com.artkuznet.converter.ldb.trigger.Trigger;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexContainer;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.ldb.waypoint.Waypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaxLDBReader {

    private MaxLDB ldb = new MaxLDB();

    private MaxTypeReader reader;

    public MaxLDBReader(String filename) {
        this.reader = new MaxTypeReader(filename);

        parseBSP();
        parseMaterials();
        parseExits();
        parseStaticMeshes();
        parseDynamicLights();
        parseWaypoints();
        parseFSMs();
        parseCharacters();
        parseTriggers();
        parseDynamicMeshes();
        parseItems();
        parsePointLights();
        parseRooms();
    }

    public MaxLDB getLdb() {
        return ldb;
    }

    private void parseBSP() {
        reader.readByte();

        int verticesCount = (int) reader.readObject();
        for (int i = 0; i < verticesCount; i++) {
            ldb.getBsp().getVertices().add(new BSPVertex((Vertex) reader.readObject()));
        }

        reader.readByte();

        int polygonsCount = (int) reader.readObject();
        for (int i = 0; i < polygonsCount; i++) {
            ldb.getBsp().getPolygons().add(
                    new BSPPolygon(
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (Vertex) reader.readObject(),
                            (Vertex) reader.readObject()
                    ));
        }

        reader.readByte();

        int nodesCount = (int) reader.readObject();
        for (int i = 0; i < nodesCount; i++) {
            ldb.getBsp().getNodes().add(
                    new BSPNode(
                            (Vertex) reader.readObject(),
                            (Vertex) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject()
                    ));
        }

        reader.readByte();

        int indicesCount = (int) reader.readObject();
        for (int i = 0; i < indicesCount; i++) {
            ldb.getBsp().getIndices().add(new BSPPolygonIndex((int) reader.readObject()));
        }
    }

    private void parseMaterials() {
        reader.readObject();

        int texturesCount = (int) reader.readObject();
        for (int i = 0; i < texturesCount; i++) {
            ldb.getTextures().add(
                    new Texture(
                            (String) reader.readObject(),
                            (int) reader.readObject(),
                            reader.readBytes((int) reader.readObject())
                    ));
        }

        reader.readByte();

        MaterialContainer tmpMaterialContainer = new MaterialContainer();

        int materialsCount = (int) reader.readObject();
        for (int i = 0; i < materialsCount; i++) {
            int material_id = (int) reader.readObject();
            reader.readByte();
            ldb.getMaterials().add(new Material(i, material_id, (String) reader.readObject(), (String) reader.readObject()));
        }

        reader.readByte();

        int orderCount = (int) reader.readObject();
        for (int i = 0; i < orderCount; i++) {
            reader.readByte();
            String categoryName = (String) reader.readObject();
            String materialName = (String) reader.readObject();
            int materialId = (int) reader.readObject();
        }


        int categoriesCount = (int) reader.readObject();
        for (int i = 0; i < categoriesCount; i++) {
            String category_name = (String) reader.readObject();
            int categoryMaterialsCount = (int) reader.readObject();

            for (int j = 0; j < categoryMaterialsCount; j++) {
                Material material = ldb.getMaterials().findMaterialByCategoryAndName(category_name, (String) reader.readObject());

                if (Objects.isNull(material)) {
                    reader.readObject();
                    reader.readObject();
                    reader.readObject();
                    reader.readObject();

                    continue;
                }

                material.setDiffuseTexture(ldb.getTextures().findTextureByFileName((String) reader.readObject()));
                material.setAlphaTexture(ldb.getTextures().findTextureByFileName((String) reader.readObject()));
                material.setProperties((int) reader.readObject(), (int) reader.readObject());
            }
        }

        int lightmapsCount = (int) reader.readObject();
        for (int i = 0; i < lightmapsCount; i++) {
            ldb.getLightMaps().add(
                    new LightmapTexture(
                            (int) reader.readObject(),
                            (int) reader.readObject(),
                            reader.readBytes((int) reader.readObject())
                    ));
        }
    }

    private void parseExits() {
        int exitsCount = (int) reader.readObject();
        for (int i = 0; i < exitsCount; i++) {
            String exit_name = (String) reader.readObject();
            VertexContainer vertices = new VertexContainer();
            int verticesCount = (int) reader.readObject();
            for (int j = 0; j < verticesCount; j++) {
                vertices.add((Vertex) reader.readObject());
            }
            Vertex normal = (Vertex) reader.readObject();
            float[][] transform = (float[][]) reader.readObject();
            int room_id = (int) reader.readObject();
            int parent_room_id = (int) reader.readObject();
            String parent_room_name = (String) reader.readObject();

            reader.readByte();

            int cnt1 = (int) reader.readObject();
            for (int j = 0; j < cnt1; j++) {
                reader.readByte();
                int cnt2 = (int) reader.readObject();
                for (int k = 0; k < cnt2; k++) {
                    reader.readObject();
                }
            }

            ldb.getExits().add(
                    new Exit(exit_name, vertices, normal, transform, room_id, parent_room_id, parent_room_name)
            );
        }
    }

    private void parseStaticMeshes() {
        reader.readByte();

        int textureVerticesCount = (int) reader.readObject();
        for (int i = 0; i < textureVerticesCount; i++) {
            ldb.getStaticMeshes().getTextureVertices().add(
                    new TextureVertex(
                            (int) reader.readObject(),
                            (VertexUV) reader.readObject(),
                            (VertexUV) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject()
                    ));
        }

        int staticMeshesCount = (int) reader.readObject();
        for (int i = 0; i < staticMeshesCount; i++) {
            int static_mesh_id = (int) reader.readObject();

            VertexContainer vertices = new VertexContainer();

            int verticesCount = (int) reader.readObject();
            for (int j = 0; j < verticesCount; j++) {
                vertices.add((Vertex) reader.readObject());
            }

            reader.readByte();

            VertexContainer normals = new VertexContainer();

            int normalsCount = (int) reader.readObject();
            for (int j = 0; j < normalsCount; j++) {
                normals.add((Vertex) reader.readObject());
            }

            float[][] transform = (float[][]) reader.readObject();

            PolygonContainer polygons = new PolygonContainer();

            int polygonsCount = (int) reader.readObject();
            for (int j = 0; j < polygonsCount; j++) {
                polygons.add(new Polygon(
                        (int) reader.readObject(),
                        (int) reader.readObject(),
                        (int) reader.readObject(),
                        (Vertex) reader.readObject(),
                        (int) reader.readObject(),
                        ldb.getMaterials().getMaterialByIndex((int) reader.readObject()),
                        ldb.getLightMaps().getTextureById((int) reader.readObject()),
                        (float) reader.readObject(),
                        (float) reader.readObject(),
                        (int) reader.readObject()
                ));
            }

            reader.readByte();

            int cnt1 = (int) reader.readObject();
            for (int j = 0; j < cnt1; j++) {
                reader.readObject();
                reader.readObject();
            }

            ldb.getStaticMeshes().add(new StaticMesh(
                    static_mesh_id,
                    vertices,
                    normals,
                    transform,
                    polygons
            ));
        }
    }

    private void parseDynamicLights() {
        int dynamicLightsCount = (int) reader.readObject();
        for (int i = 0; i < dynamicLightsCount; i++) {
            ldb.getDynamicLights().add(new DynamicLight(
                    (String) reader.readObject(),
                    new EntityProperties(
                            (String) reader.readObject(),
                            (float[][]) reader.readObject(),
                            (float[][]) reader.readObject(),
                            (int) reader.readObject(),
                            (String) reader.readObject()
                    ),
                    (float[][]) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject(),
                    (float) reader.readObject()
            ));
        }
    }

    private void parseWaypoints() {
        int waypointsCount = (int) reader.readObject();
        for (int i = 0; i < waypointsCount; i++) {
            ldb.getWaypoints().add(new Waypoint(
                            (String) reader.readObject(),
                            new EntityProperties(
                                    (String) reader.readObject(),
                                    (float[][]) reader.readObject(),
                                    (float[][]) reader.readObject(),
                                    (int) reader.readObject(),
                                    (String) reader.readObject()
                            ),
                            (int) reader.readObject()
                    )
            );
        }
    }

    private void parseFSMs() {
        int fsmsCount = (int) reader.readObject();
        for (int i = 0; i < fsmsCount; i++) {
            String shared_name = (String) reader.readObject();

            EntityProperties properties = new EntityProperties(
                    (String) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject(),
                    (String) reader.readObject()
            );

            reader.readByte();

            FSMStateContainer states = new FSMStateContainer();

            int fsmStatesCount = (int) reader.readObject();
            for (int j = 0; j < fsmStatesCount; j++) {
                states.add((String) reader.readObject());
            }

            states.setDefault((String) reader.readObject());

            reader.readByte();

            FSMMessageContainer startup_before = new FSMMessageContainer();

            int fsmMessagesBeforeCount = (int) reader.readObject();
            for (int j = 0; j < fsmMessagesBeforeCount; j++) {
                startup_before.add((String) reader.readObject());
            }

            reader.readByte();
            int unkCount = (int) reader.readObject();
            for (int j = 0; j < unkCount; j++) {
                reader.readObject();
            }

            reader.readByte();

            FSMMessageContainer startup_after = new FSMMessageContainer();

            int fsmMessagesAfterCount = (int) reader.readObject();
            for (int j = 0; j < fsmMessagesAfterCount; j++) {
                startup_after.add((String) reader.readObject());
            }

            reader.readByte();

            FSMEventContainer state_switch = new FSMEventContainer();
            int fsmStateSwitchesCount = (int) reader.readObject();
            for (int j = 0; j < fsmStateSwitchesCount; j++) {
                String state_name = (String) reader.readObject();

                reader.readByte();
                FSMMessageContainer before = new FSMMessageContainer();
                int fsmStateSwitchBeforeCount = (int) reader.readObject();
                for (int k = 0; k < fsmStateSwitchBeforeCount; k++) {
                    before.add((String) reader.readObject());
                }

                reader.readByte();
                FSMStateSpecificMessageContainer state_specific = new FSMStateSpecificMessageContainer();
                int fsmStateSpecificCount = (int) reader.readObject();
                for (int k = 0; k < fsmStateSpecificCount; k++) {
                    String state_name_specific = (String) reader.readObject();

                    reader.readByte();

                    FSMMessageContainer messages = new FSMMessageContainer();

                    int fsmStateSpecificMessagesCount = (int) reader.readObject();
                    for (int l = 0; l < fsmStateSpecificMessagesCount; l++) {
                        messages.add((String) reader.readObject());
                    }

                    state_specific.add(new FSMStateSpecificMessage(state_name_specific, messages));
                }

                reader.readByte();
                FSMMessageContainer after = new FSMMessageContainer();
                int fsmSpecificMessagesAfterCount = (int) reader.readObject();
                for (int k = 0; k < fsmSpecificMessagesAfterCount; k++) {
                    after.add((String) reader.readObject());
                }
                state_switch.add(new FSMEvent(state_name, before, state_specific, after));
            }

            reader.readByte();
            FSMEventContainer custom_string = new FSMEventContainer();


            int fsmCustomStringMessagesCount = (int) reader.readObject();
            for (int j = 0; j < fsmCustomStringMessagesCount; j++) {

                String state_name = (String) reader.readObject();

                reader.readByte();
                FSMMessageContainer before = new FSMMessageContainer();
                int cnt1 = (int) reader.readObject();
                for (int k = 0; k < cnt1; k++) {
                    before.add((String) reader.readObject());
                }

                reader.readByte();

                FSMStateSpecificMessageContainer state_specific = new FSMStateSpecificMessageContainer();

                int cnt2 = (int) reader.readObject();
                for (int k = 0; k < cnt2; k++) {
                    String state_name_1 = (String) reader.readObject();
                    reader.readByte();
                    FSMMessageContainer messages = new FSMMessageContainer();
                    int cnt3 = (int) reader.readObject();
                    for (int l = 0; l < cnt3; l++) {
                        messages.add((String) reader.readObject());
                    }
                    state_specific.add(new FSMStateSpecificMessage(state_name_1, messages));
                }

                reader.readByte();
                FSMMessageContainer after = new FSMMessageContainer();
                int cnt4 = (int) reader.readObject();
                for (int k = 0; k < cnt4; k++) {
                    after.add((String) reader.readObject());
                }
                custom_string.add(new FSMEvent(state_name, before, state_specific, after));
            }

            reader.readByte();
            FSMEventContainer entity_specific = new FSMEventContainer();

            int entitySpecificMessagesCount = (int) reader.readObject();
            for (int j = 0; j < entitySpecificMessagesCount; j++) {
                String state_name = (String) reader.readObject();

                reader.readByte();
                FSMMessageContainer before = new FSMMessageContainer();
                int cnt1 = (int) reader.readObject();
                for (int k = 0; k < cnt1; k++) {
                    before.add((String) reader.readObject());
                }

                reader.readByte();
                FSMStateSpecificMessageContainer state_specific = new FSMStateSpecificMessageContainer();

                int cnt2 = (int) reader.readObject();
                for (int k = 0; k < cnt2; k++) {
                    String state_name_1 = (String) reader.readObject();
                    reader.readByte();
                    FSMMessageContainer messages = new FSMMessageContainer();
                    int cnt3 = (int) reader.readObject();
                    for (int l = 0; l < cnt3; l++) {
                        messages.add((String) reader.readObject());
                    }
                    state_specific.add(new FSMStateSpecificMessage(state_name_1, messages));
                }

                reader.readByte();
                FSMMessageContainer after = new FSMMessageContainer();
                int cnt5 = (int) reader.readObject();

                for (int k = 0; k < cnt5; k++) {
                    after.add((String) reader.readObject());
                }
                entity_specific.add(new FSMEvent(state_name, before, state_specific, after));
            }

            ldb.getFSMs().add(
                    new LdbFSM(
                            shared_name,
                            properties,
                            states,
                            startup_before,
                            startup_after,
                            state_switch,
                            custom_string,
                            entity_specific
                    )
            );
        }
    }

    private void parseCharacters() {
        int charactersCount = (int) reader.readObject();
        for (int i = 0; i < charactersCount; i++) {
            String shared_name = (String) reader.readObject();

            EntityProperties properties = new EntityProperties(
                    (String) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject(),
                    (String) reader.readObject()
            );

            String character_name = (String) reader.readObject();

            reader.readByte();

            FSMMessageContainer startup_before = new FSMMessageContainer();
            int cnt1 = (int) reader.readObject();
            for (int j = 0; j < cnt1; j++) {
                startup_before.add((String) reader.readObject());
            }

            reader.readByte();

            FSMMessageContainer on_death_before = new FSMMessageContainer();
            int cnt2 = (int) reader.readObject();
            for (int j = 0; j < cnt2; j++) {
                on_death_before.add((String) reader.readObject());
            }

            reader.readByte();

            FSMMessageContainer on_activate_before = new FSMMessageContainer();
            int cnt3 = (int) reader.readObject();
            for (int j = 0; j < cnt3; j++) {
                on_activate_before.add((String) reader.readObject());
            }

            reader.readByte();

            FSMMessageContainer on_special_before = new FSMMessageContainer();

            int cnt4 = (int) reader.readObject();
            for (int j = 0; j < cnt4; j++) {
                on_special_before.add((String) reader.readObject());
            }

            ldb.getCharacters().add(
                    new Character(
                            shared_name,
                            properties,
                            character_name,
                            startup_before,
                            on_death_before,
                            on_activate_before,
                            on_special_before
                    )
            );
        }
    }

    private void parseTriggers() {
        int triggersCount = (int) reader.readObject();
        for (int i = 0; i < triggersCount; i++) {
            String shared_name = (String) reader.readObject();
            EntityProperties properties = new EntityProperties(
                    (String) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject(),
                    (String) reader.readObject()
            );

            float radius = (float) reader.readObject();

            // 0 - action_button 3 - character_collide 4 - look_at_trigger 1 - player_collide 2 - projectile_collide
            int type = (int) reader.readObject();
            ldb.getTriggers().add(new Trigger(shared_name, properties, radius, type));
        }
    }

    private void parseDynamicMeshes() {
        reader.readByte();

        int cnt1 = (int) reader.readObject();
        for (int i = 0; i < cnt1; i++) {
            ldb.getDynamicMeshes().getTextureVertices().add(
                    new TextureVertex(
                            (int) reader.readObject(),
                            (VertexUV) reader.readObject(),
                            (VertexUV) reader.readObject(),
                            (int) reader.readObject(),
                            (int) reader.readObject()
                    ));
        }

        int cnt2 = (int) reader.readObject();
        for (int i = 0; i < cnt2; i++) {
            String shared_name = (String) reader.readObject();

            VertexContainer vertices = new VertexContainer();
            int cnt3 = (int) reader.readObject();
            for (int j = 0; j < cnt3; j++) {
                vertices.add((Vertex) reader.readObject());
            }

            reader.readByte();

            VertexContainer normals = new VertexContainer();
            int cnt4 = (int) reader.readObject();
            for (int j = 0; j < cnt4; j++) {
                normals.add((Vertex) reader.readObject());
            }

            float[][] transform = (float[][]) reader.readObject();

            PolygonContainer polygons = new PolygonContainer();
            int cnt5 = (int) reader.readObject();
            for (int j = 0; j < cnt5; j++) {
                polygons.add(new Polygon(
                        (int) reader.readObject(),
                        (int) reader.readObject(),
                        (int) reader.readObject(),
                        (Vertex) reader.readObject(),
                        (int) reader.readObject(),
                        ldb.getMaterials().getMaterialByIndex((int) reader.readObject()),
                        ldb.getLightMaps().getTextureById((int) reader.readObject()),
                        (float) reader.readObject(),
                        (float) reader.readObject(),
                        (int) reader.readObject()
                ));
            }

            reader.readByte();

            Object unk1 = reader.readObject();
            EntityProperties properties = new EntityProperties(
                    (String) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (float[][]) reader.readObject(),
                    (int) reader.readObject(),
                    (String) reader.readObject()
            );

            AnimationContainer animations = new AnimationContainer();
            int cnt6 = (int) reader.readObject();
            for (int j = 0; j < cnt6; j++) {
                String animation_name = (String) reader.readObject();
                float length_in_secs = (float) reader.readObject();
                float[][] start_transform = (float[][]) reader.readObject();
                float[][] end_transform = (float[][]) reader.readObject();

                reader.readByte();

                FSMMessageContainer leaving_first_frame = new FSMMessageContainer();

                int cnt7 = (int) reader.readObject();
                for (int k = 0; k < cnt7; k++) {
                    leaving_first_frame.add((String) reader.readObject());
                }

                reader.readByte();

                FSMMessageContainer returning_first_frame = new FSMMessageContainer();
                int cnt8 = (int) reader.readObject();
                for (int k = 0; k < cnt8; k++) {
                    returning_first_frame.add((String) reader.readObject());
                }

                reader.readByte();

                FSMMessageContainer reaching_second_frame = new FSMMessageContainer();
                int cnt9 = (int) reader.readObject();
                for (int k = 0; k < cnt9; k++) {
                    reaching_second_frame.add((String) reader.readObject());
                }

                reader.readObject();
                reader.readObject();
                reader.readObject();
                int sample_rate_translation = (int) reader.readObject();

                Graph translation_graph = new Graph(sample_rate_translation);

                for (int k = 0; k < sample_rate_translation; k++) {
                    translation_graph.addPoint((float) reader.readObject());
                }

                reader.readObject();
                reader.readObject();
                reader.readObject();
                int sample_rate_rotation = (int) reader.readObject();
                Graph rotation_graph = new Graph(sample_rate_rotation);
                for (int k = 0; k < sample_rate_rotation; k++) {
                    rotation_graph.addPoint((float) reader.readObject());
                }

                animations.add(new Animation(
                        animation_name,
                        length_in_secs,
                        start_transform,
                        end_transform,
                        leaving_first_frame,
                        returning_first_frame,
                        reaching_second_frame,
                        translation_graph,
                        rotation_graph
                ));
            }

            DynamicMeshConfig config = new DynamicMeshConfig(
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject(),
                    (int) reader.readObject()
            );

            reader.readObject();
            reader.readObject();
            reader.readObject();
            reader.readObject();

            ldb.getDynamicMeshes().addDynamicMesh(new LdbDynamicMesh(
                    shared_name,
                    properties,
                    vertices,
                    normals,
                    transform,
                    polygons,
                    animations,
                    config
            ));
        }
    }

    private void parseItems() {
        int itemsCount = (int) reader.readObject();
        for (int i = 0; i < itemsCount; i++) {
            ldb.getItems().add(
                    new Item(
                            (String) reader.readObject(),
                            new EntityProperties(
                                    (String) reader.readObject(),
                                    (float[][]) reader.readObject(),
                                    (float[][]) reader.readObject(),
                                    (int) reader.readObject(),
                                    (String) reader.readObject()
                            ),
                            (String) reader.readObject()
                    )
            );
        }
    }

    private void parsePointLights() {
        int pointLightsCount = (int) reader.readObject();
        for (int i = 0; i < pointLightsCount; i++) {
            ldb.getPointlights().add(
                    new PointLight(
                            (int) reader.readObject(),
                            new EntityProperties(
                                    (String) reader.readObject(),
                                    (float[][]) reader.readObject(),
                                    (float[][]) reader.readObject(),
                                    (int) reader.readObject(),
                                    (String) reader.readObject()
                            ),
                            (float) reader.readObject(),
                            (float) reader.readObject(),
                            (float) reader.readObject(),
                            (float) reader.readObject(),
                            (float) reader.readObject(),
                            (float) reader.readObject()
                    )
            );
        }
    }

    private void parseRooms() {
        int cnt1 = (int) reader.readObject();
        for (int i = 0; i < cnt1; i++) {
            int id = (int) reader.readObject();

            reader.readByte();
            List<Integer> static_meshes = new ArrayList<>();

            int cnt2 = (int) reader.readObject();
            for (int j = 0; j < cnt2; j++) {
                static_meshes.add((int) reader.readObject());
            }

            reader.readByte();
            List<String> dynamic_lights = new ArrayList<>();

            int cnt3 = (int) reader.readObject();
            for (int j = 0; j < cnt3; j++) {
                dynamic_lights.add((String) reader.readObject());
            }

            reader.readByte();
            List<String> exits = new ArrayList<>();

            int cnt4 = (int) reader.readObject();
            for (int j = 0; j < cnt4; j++) {
                exits.add((String) reader.readObject());

            }

            reader.readByte();
            List<String> start_points = new ArrayList<>();

            int cnt5 = (int) reader.readObject();
            for (int j = 0; j < cnt5; j++) {
                start_points.add((String) reader.readObject());
            }

            reader.readByte();
            List<String> fsms = new ArrayList<>();

            int cnt6 = (int) reader.readObject();
            for (int j = 0; j < cnt6; j++) {
                fsms.add((String) reader.readObject());
            }

            reader.readByte();
            List<String> characters = new ArrayList<>();

            int cnt7 = (int) reader.readObject();
            for (int j = 0; j < cnt7; j++) {
                characters.add((String) reader.readObject());
            }

            reader.readByte();
            List<String> triggers = new ArrayList<>();

            int cnt8 = (int) reader.readObject();
            for (int j = 0; j < cnt8; j++) {
                triggers.add((String) reader.readObject());
            }

            reader.readByte();
            List<String> dynamic_meshes = new ArrayList<>();

            int cnt9 = (int) reader.readObject();
            for (int j = 0; j < cnt9; j++) {
                dynamic_meshes.add((String) reader.readObject());
            }

            reader.readByte();
            List<String> level_items = new ArrayList<>();

            int cnt10 = (int) reader.readObject();
            for (int j = 0; j < cnt10; j++) {
                level_items.add((String) reader.readObject());
            }

            reader.readByte();
            List<Integer> point_lights = new ArrayList<>();

            int cnt11 = (int) reader.readObject();
            for (int j = 0; j < cnt11; j++) {
                point_lights.add((int) reader.readObject());

            }

            String room_name = (String) reader.readObject();

            float aiNetDensity = (float) reader.readObject();

            ldb.getRooms().add(new Room(
                    id,
                    room_name,
                    static_meshes,
                    dynamic_lights,
                    exits,
                    start_points,
                    fsms,
                    characters,
                    triggers,
                    dynamic_meshes,
                    level_items,
                    point_lights,
                    aiNetDensity
            ));

            reader.readObject();
            reader.readObject();
            reader.readObject();
            reader.readObject();
        }
    }
}
