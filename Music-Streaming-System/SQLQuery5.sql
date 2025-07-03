use MusicSystemDB;

select * from admin;

select * from analytics_report;
select * from artist;
select * from album;
select * from live_concerts;
select * from song;
select * from song_queue;
select * from users;
select * from listening_history;
select * from play_list;
select * from notification;

select * from user_following_artists;
select * from play_List_songs;
update users set current_song_queue_id=1 where id=1
update song set status = 'PENDING' where id =18
delete from user_following_artists where user_id=8;
select song_id from song_queue where user_id=1 and current_position=2

select * from song where status='APPROVED' 

delete artist where id=8

delete users where id=9

delete song where id=23

delete play_list where id=8;

delete play_List_songs where play_list_id=9 and song_id=6;

select current_position from song_queue where user_id=1

delete user_following_artists where user_id=1 and artist_id=1;

delete listening_history where id=704

update song set genre='Romantic' where id=6

delete song_queue where id=188;

update song set play_count='89' where id=10

update artist set total_play_count=130 where id=2;

delete admin where id=2

CREATE INDEX idx_artists_name ON artist(name);


CREATE INDEX idx_songs_genre ON song(genre);

CREATE INDEX idx_songs_language ON song(language);


CREATE INDEX idx_songs_status ON song(status);

CREATE INDEX idx_songs_artist_id ON song(artist_id);

CREATE INDEX idx_artists_email ON artist(email);

CREATE INDEX idx_song_queue_song_id ON song_queue(song_id);

CREATE INDEX idx_song_queue_user_id ON song_queue(user_id);

