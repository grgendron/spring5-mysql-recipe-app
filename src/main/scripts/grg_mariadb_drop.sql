alter table ingredient drop foreign key FKhe4603p2jtb5x56jvk8k4i2yg;
alter table ingredient drop foreign key FKj0s4ywmqqqw4h5iommigh5yja;
alter table ingredient drop foreign key FK6iv5l89qmitedn5m2a71kta2t;
alter table notes drop foreign key FKdbfsiv21ocsbt63sd6fg0t3c8;
alter table recipe drop foreign key FK37al6kcbdasgfnut9xokktie9;
alter table recipe_category drop foreign key FKqsi87i8d4qqdehlv2eiwvpwb;
alter table recipe_category drop foreign key FKcqlqnvfyarhieewfeayk3v25v;
drop table if exists base_ingredient;
drop table if exists category;
drop table if exists ingredient;
drop table if exists notes;
drop table if exists recipe;
drop table if exists recipe_category;
drop table if exists unit_of_measure;
