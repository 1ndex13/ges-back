insert into roles
values
--    (1, 'Роль пользователя', 'USER'),
    (2, 'Роль библиотекаря', 'LIBRARIAN');

--АВТОРЫ
drop sequence authors_sequence;
create sequence authors_sequence;
alter sequence authors_sequence owner to postgres;


truncate table books_authors;
truncate table authors cascade;
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:46:11.797607', 'Александр Грибоедов', 'Писатель Александр Грибоедов ' ||
                                                                                              'родился в 1795 году в Москве. С детства Александр был невероятно развитым мальчиком — в шесть
                     лет он знал уже три языка, а еще через 6 — еще три.', '1795-01-15');
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:47:02.414728', 'Джейн Остин',
        'Остен Джейн — выдающий британский писатель, классик мировой литературы. Родилась в 1775 году в небольшом городке в графстве Хэмпшир. В семье кроме Джейн росло еще 7 детей. У будущей романистки было 6 братьев и сестра Кассандра. ',
        '1775-12-16');
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:48:53.363059', 'Федор Достоевский',
        'Гордость отечественной литературы Федор Михайлович Достоевский родился в Москве в 1821 году в семье врача. Почти все свое детство Федор и его шесть братьев и сестер провели внутри больничных стен на работе отца. Воспитывались дети в строгости.',
        '1821-11-11');
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:50:12.953413', 'Эмили Бронте',
        'член знаменитого литературного английского семейства, при жизни была, пожалуй, самой незаметной на фоне своих знаменитых сестры и брата. С детства отличаясь живым и ярким воображением',
        '1818-07-30');
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Михаил Булгаков',
        'Михаил Афанасьевич Булгаков появился на свет 15 мая (по старому стилю — 3 мая) 1891 года в Киеве. Его родители были преподавателями и сделали все, чтобы дать сыну блестящее образование.',
        '1891-05-15');
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Илья Ильф', 'настоящее имя — Иехиел-Лейб бен Арьевич Файнзильберг; 1897—1937', '1897-01-01');
INSERT INTO authors (id, created_by, created_when, name, description, birth_date)
VALUES (nextval('public.authors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Евгений Петров', 'настоящее имя — Евгений Петрович Катаев; 1902—1942', '1902-01-01');

select * from book_rent_info


--BOOKS
drop sequence books_sequence;
create sequence books_sequence;
alter sequence books_sequence owner to postgres;
truncate table books cascade;
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:54:28.115079', 100, 1, null, '1966-01-01', '1-М', 'Мастер и Маргарита', 'журнал «Москва»', 416);
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:56:12.600618', 10, 2, null, '1925-01-01', '1-Б', 'Белая Гвардия', 'журнал «Россия»', 416);
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:57:05.231780', 11, 3, null, '2023-01-15', '17-Г', 'Грозовой перевал', 'Азбука, 2023 г.', 384);
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:57:43.883671', 11, 4, null, '2022-01-01', '1-И', 'Идиот', 'Эксмо', 636);
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:58:12.172216', 111, 3, null, '2022-03-22', '13-Г', 'Гордость и предубеждение', 'АСТ', 416);
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:58:12.172216', 110, 2, null, '2016-03-11', '1-Г', 'Горе от ума', 'Азбука', 256);
INSERT INTO books (id, created_by, created_when, amount, genre, online_copy_path, publish_date, storage_place, title, publisher, page_count)
VALUES (nextval('public.books_sequence'), 'admin', '2022-11-15 13:58:12.172216', 2, 1, null, '2016-03-11', '1-Г', 'Золотой теленок', 'Текст', 432);

--BOOKS_AUTHORS
INSERT INTO public.books_authors (book_id, author_id)
VALUES (1, 5);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (2, 5);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (3, 4);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (4, 3);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (5, 2);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (6, 1);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (7, 6);
INSERT INTO public.books_authors (book_id, author_id)
VALUES (7, 7);

insert into public.users(id, username, email, password, confirm_password)
values  (5, 'user', 'tyty@tyty.ru', 123, 123)