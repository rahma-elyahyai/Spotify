use MusicSystemDB2;



select * from analytics_report;

select * from album;
select * from live_concerts;
select * from song;
select * from song_queue;
select * from users;
select * from play_list;
select * from account;
select * from artist;
select * from admin;

select * from user_following_artists;

select top 5 * from artist a order by a.total_play_count desc;
select top 5 * from song s where status='APPROVED' order by s.play_count desc;

CREATE INDEX idx_artists_name ON artist(name);

DROP INDEX idx_artists_name ON artist;

CREATE INDEX idx_songs_genre ON song(genre);

CREATE INDEX idx_songs_language ON song(language);


CREATE INDEX idx_songs_status ON song(status);

CREATE INDEX idx_songs_artist_id ON song(artist_id);


CREATE INDEX idx_account_email ON account(email);

CREATE INDEX idx_song_queue_song_id ON song_queue(song_id);

CREATE INDEX idx_song_queue_user_id ON song_queue(user_id);

update song set genre='Romantic' where id=9

delete song where id=25


update song set play_count = 14 where id=7;
update song set play_count = 24 where id=8;
update song set play_count = 54 where id=2;
update song set play_count = 78 where id=1;

update song set status='PENDING' where id=18;
delete album where id=6;
delete artist  where id=6;
delete account where id=23;
delete song where id=36;
delete play_list where id=5
