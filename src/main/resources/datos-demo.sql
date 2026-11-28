-- Datos iniciales del catálogo para el despliegue (ejecutar una sola vez, después de import.sql)
-- docker exec -i faunascan-db psql -U postgres -d FaunaScanDB < src/main/resources/datos-demo.sql
-- Categorías según la Lista Roja de la UICN. El sistema considera críticas las que contienen "Vulnerable" o "peligro" (HU-11)

INSERT INTO categorias_conservacion (nombre, descripcion) VALUES ('Preocupación menor', 'Especie con poblaciones estables y amplia distribución');
INSERT INTO categorias_conservacion (nombre, descripcion) VALUES ('Casi amenazado', 'Especie cercana a cumplir los criterios de amenaza');
INSERT INTO categorias_conservacion (nombre, descripcion) VALUES ('Vulnerable', 'Especie con alto riesgo de extinción en estado silvestre');
INSERT INTO categorias_conservacion (nombre, descripcion) VALUES ('En peligro', 'Especie con riesgo muy alto de extinción en estado silvestre');
INSERT INTO categorias_conservacion (nombre, descripcion) VALUES ('En peligro crítico', 'Especie con riesgo extremadamente alto de extinción en estado silvestre');

INSERT INTO familias (nombre, descripcion) VALUES ('Ursidae', 'Osos');
INSERT INTO familias (nombre, descripcion) VALUES ('Cathartidae', 'Buitres del Nuevo Mundo');
INSERT INTO familias (nombre, descripcion) VALUES ('Atelidae', 'Monos aulladores, araña y choros');
INSERT INTO familias (nombre, descripcion) VALUES ('Cotingidae', 'Cotingas');
INSERT INTO familias (nombre, descripcion) VALUES ('Camelidae', 'Camélidos');
INSERT INTO familias (nombre, descripcion) VALUES ('Felidae', 'Felinos');
INSERT INTO familias (nombre, descripcion) VALUES ('Mustelidae', 'Nutrias, hurones y afines');
INSERT INTO familias (nombre, descripcion) VALUES ('Spheniscidae', 'Pingüinos');
INSERT INTO familias (nombre, descripcion) VALUES ('Psittacidae', 'Loros y guacamayos');
INSERT INTO familias (nombre, descripcion) VALUES ('Iniidae', 'Delfines de río');
INSERT INTO familias (nombre, descripcion) VALUES ('Telmatobiidae', 'Ranas andinas acuáticas');
INSERT INTO familias (nombre, descripcion) VALUES ('Tapiridae', 'Tapires');

INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Ursidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Vulnerable'), 'Oso de anteojos', 'Tremarctos ornatus', 'Pelaje negro o pardo oscuro con manchas claras alrededor de los ojos y en el pecho.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Cathartidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Vulnerable'), 'Cóndor andino', 'Vultur gryphus', 'Ave negra muy grande con collar blanco y cabeza sin plumas; planea sin aletear.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Atelidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'En peligro crítico'), 'Mono choro de cola amarilla', 'Lagothrix flavicauda', 'Pelaje pardo rojizo, mancha clara en el hocico y franja amarilla bajo la cola.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Cotingidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Preocupación menor'), 'Gallito de las rocas', 'Rupicola peruvianus', 'Macho naranja intenso con cresta en abanico que cubre el pico; alas negras.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Camelidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Preocupación menor'), 'Vicuña', 'Vicugna vicugna', 'Camélido esbelto color canela con pecho y vientre blancos; vive sobre los 3500 m.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Felidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Casi amenazado'), 'Jaguar', 'Panthera onca', 'Felino grande amarillo con rosetas negras que tienen puntos en su interior.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Mustelidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'En peligro'), 'Lobo de río', 'Pteronura brasiliensis', 'Nutria grande de pelaje marrón con una mancha clara distinta en la garganta.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Spheniscidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Vulnerable'), 'Pingüino de Humboldt', 'Spheniscus humboldti', 'Dorso negro, vientre blanco y una sola banda negra en forma de herradura en el pecho.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Psittacidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Preocupación menor'), 'Guacamayo rojo y verde', 'Ara chloropterus', 'Cuerpo rojo con franja verde en las alas y líneas rojas en la cara blanca.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Iniidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'En peligro'), 'Delfín rosado', 'Inia geoffrensis', 'Delfín de río de color rosado o gris, hocico largo y joroba en lugar de aleta dorsal.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Telmatobiidae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'En peligro crítico'), 'Rana gigante del Titicaca', 'Telmatobius culeus', 'Rana acuática con piel muy arrugada y suelta; vive en el lago Titicaca.');
INSERT INTO especies (id_familia, id_categoria, nombre_comun, nombre_cientifico, pistas_identificacion) VALUES ((SELECT id_familia FROM familias WHERE nombre = 'Tapiridae'), (SELECT id_categoria FROM categorias_conservacion WHERE nombre = 'Vulnerable'), 'Sachavaca', 'Tapirus terrestris', 'Mamífero grande y oscuro con trompa corta y cresta de pelo en la nuca.');

-- Áreas naturales protegidas usadas como ubicaciones de referencia
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-11.856000, -71.721000, 'Parque Nacional del Manu, Madre de Dios');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-12.850000, -69.570000, 'Reserva Nacional Tambopata, Madre de Dios');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-13.830000, -76.250000, 'Reserva Nacional de Paracas, Ica');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-14.680000, -74.400000, 'Reserva Nacional Pampa Galeras, Ayacucho');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-15.612200, -71.906000, 'Valle del Colca, Arequipa');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-5.000000, -74.600000, 'Reserva Nacional Pacaya Samiria, Loreto');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-15.800000, -69.800000, 'Reserva Nacional del Titicaca, Puno');
INSERT INTO ubicaciones (latitud, longitud, direccion) VALUES (-9.120000, -77.600000, 'Parque Nacional Huascarán, Áncash');

-- Fotos de referencia del catálogo (Wikimedia Commons). Este bloque también se puede ejecutar solo en una base ya cargada
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Spectacled_Bear_-_Houston_Zoo.jpg?width=640' WHERE nombre_cientifico = 'Tremarctos ornatus';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Colca-condor-c03.jpg?width=640' WHERE nombre_cientifico = 'Vultur gryphus';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Andean_cock-of-the-rock_%28Rupicola_peruvianus_sanguinolentus%29_male_Antioquia.jpg?width=640' WHERE nombre_cientifico = 'Rupicola peruvianus';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Vicugna_vicugna_Salar_de_Chalviri.jpg?width=640' WHERE nombre_cientifico = 'Vicugna vicugna';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Jaguar_%28Panthera_onca_palustris%29_male_Rio_Negro_2.JPG?width=640' WHERE nombre_cientifico = 'Panthera onca';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Giantotter.jpg?width=640' WHERE nombre_cientifico = 'Pteronura brasiliensis';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Spheniscus_humboldti_20070116.jpg?width=640' WHERE nombre_cientifico = 'Spheniscus humboldti';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Red-and-green_macaw_%28Ara_chloropterus%29_juvenile.JPG?width=640' WHERE nombre_cientifico = 'Ara chloropterus';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Inia.jpg?width=640' WHERE nombre_cientifico = 'Inia geoffrensis';
UPDATE especies SET imagen_referencia = 'https://commons.wikimedia.org/wiki/Special:FilePath/Tapirus_terrestris_1.jpg?width=640' WHERE nombre_cientifico = 'Tapirus terrestris';
