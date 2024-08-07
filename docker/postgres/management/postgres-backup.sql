PGDMP      $    
            |         	   findfirst    16.2    16.2 ?    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16384 	   findfirst    DATABASE     t   CREATE DATABASE findfirst WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE findfirst;
                postgres    false            �            1259    16386    bookmark    TABLE     W  CREATE TABLE public.bookmark (
    tenant_id integer NOT NULL,
    created_date timestamp(6) without time zone,
    id bigint NOT NULL,
    last_modified_date timestamp(6) without time zone,
    title character varying(255),
    created_by character varying(255),
    last_modified_by character varying(255),
    url character varying(255)
);
    DROP TABLE public.bookmark;
       public         heap    postgres    false            �            1259    16385    bookmark_id_seq    SEQUENCE     x   CREATE SEQUENCE public.bookmark_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.bookmark_id_seq;
       public          postgres    false    216            �           0    0    bookmark_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.bookmark_id_seq OWNED BY public.bookmark.id;
          public          postgres    false    215            �            1259    16430    bookmark_tag    TABLE     b   CREATE TABLE public.bookmark_tag (
    bookmark_id bigint NOT NULL,
    tag_id bigint NOT NULL
);
     DROP TABLE public.bookmark_tag;
       public         heap    postgres    false            �            1259    16435    refreshtoken    TABLE     �   CREATE TABLE public.refreshtoken (
    user_id integer,
    expiry_date timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    token character varying(255) NOT NULL
);
     DROP TABLE public.refreshtoken;
       public         heap    postgres    false            �            1259    16450    refreshtoken_seq    SEQUENCE     z   CREATE SEQUENCE public.refreshtoken_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.refreshtoken_seq;
       public          postgres    false            �            1259    16443    roles    TABLE       CREATE TABLE public.roles (
    role_id integer NOT NULL,
    name character varying(20),
    CONSTRAINT roles_name_check CHECK (((name)::text = ANY ((ARRAY['ROLE_USER'::character varying, 'ROLE_MODERATOR'::character varying, 'ROLE_ADMIN'::character varying])::text[])))
);
    DROP TABLE public.roles;
       public         heap    postgres    false            �            1259    16442    roles_role_id_seq    SEQUENCE     �   CREATE SEQUENCE public.roles_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.roles_role_id_seq;
       public          postgres    false    227            �           0    0    roles_role_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.roles_role_id_seq OWNED BY public.roles.role_id;
          public          postgres    false    226            �            1259    16395    tag    TABLE     5  CREATE TABLE public.tag (
    tenant_id integer NOT NULL,
    created_date timestamp(6) without time zone,
    id bigint NOT NULL,
    last_modified_date timestamp(6) without time zone,
    tag_title character varying(50),
    created_by character varying(255),
    last_modified_by character varying(255)
);
    DROP TABLE public.tag;
       public         heap    postgres    false            �            1259    16394 
   tag_id_seq    SEQUENCE     s   CREATE SEQUENCE public.tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.tag_id_seq;
       public          postgres    false    218            �           0    0 
   tag_id_seq    SEQUENCE OWNED BY     9   ALTER SEQUENCE public.tag_id_seq OWNED BY public.tag.id;
          public          postgres    false    217            �            1259    16404    tenants    TABLE       CREATE TABLE public.tenants (
    id integer NOT NULL,
    created_date timestamp(6) without time zone,
    last_modified_date timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_by character varying(255),
    name character varying(255) NOT NULL
);
    DROP TABLE public.tenants;
       public         heap    postgres    false            �            1259    16403    tenants_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tenants_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.tenants_id_seq;
       public          postgres    false    220            �           0    0    tenants_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.tenants_id_seq OWNED BY public.tenants.id;
          public          postgres    false    219            �            1259    16451    tenants_seq    SEQUENCE     u   CREATE SEQUENCE public.tenants_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.tenants_seq;
       public          postgres    false            �            1259    16412    token    TABLE     �   CREATE TABLE public.token (
    expiry_date date,
    user_id integer NOT NULL,
    id bigint NOT NULL,
    token character varying(255)
);
    DROP TABLE public.token;
       public         heap    postgres    false            �            1259    16452 	   token_seq    SEQUENCE     s   CREATE SEQUENCE public.token_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
     DROP SEQUENCE public.token_seq;
       public          postgres    false            �            1259    16420    users    TABLE     �   CREATE TABLE public.users (
    enabled boolean,
    role_role_id integer NOT NULL,
    tenant_id integer NOT NULL,
    user_id integer NOT NULL,
    username character varying(20),
    email character varying(50),
    password character varying(255)
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    16419    users_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.users_user_id_seq;
       public          postgres    false    223            �           0    0    users_user_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;
          public          postgres    false    222            �           2604    16389    bookmark id    DEFAULT     j   ALTER TABLE ONLY public.bookmark ALTER COLUMN id SET DEFAULT nextval('public.bookmark_id_seq'::regclass);
 :   ALTER TABLE public.bookmark ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215    216            �           2604    16446    roles role_id    DEFAULT     n   ALTER TABLE ONLY public.roles ALTER COLUMN role_id SET DEFAULT nextval('public.roles_role_id_seq'::regclass);
 <   ALTER TABLE public.roles ALTER COLUMN role_id DROP DEFAULT;
       public          postgres    false    227    226    227            �           2604    16398    tag id    DEFAULT     `   ALTER TABLE ONLY public.tag ALTER COLUMN id SET DEFAULT nextval('public.tag_id_seq'::regclass);
 5   ALTER TABLE public.tag ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    218    218            �           2604    16407 
   tenants id    DEFAULT     h   ALTER TABLE ONLY public.tenants ALTER COLUMN id SET DEFAULT nextval('public.tenants_id_seq'::regclass);
 9   ALTER TABLE public.tenants ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    220    220            �           2604    16423    users user_id    DEFAULT     n   ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);
 <   ALTER TABLE public.users ALTER COLUMN user_id DROP DEFAULT;
       public          postgres    false    222    223    223            �          0    16386    bookmark 
   TABLE DATA           }   COPY public.bookmark (tenant_id, created_date, id, last_modified_date, title, created_by, last_modified_by, url) FROM stdin;
    public          postgres    false    216            �          0    16430    bookmark_tag 
   TABLE DATA           ;   COPY public.bookmark_tag (bookmark_id, tag_id) FROM stdin;
    public          postgres    false    224            �          0    16435    refreshtoken 
   TABLE DATA           G   COPY public.refreshtoken (user_id, expiry_date, id, token) FROM stdin;
    public          postgres    false    225            �          0    16443    roles 
   TABLE DATA           .   COPY public.roles (role_id, name) FROM stdin;
    public          postgres    false    227            �          0    16395    tag 
   TABLE DATA           w   COPY public.tag (tenant_id, created_date, id, last_modified_date, tag_title, created_by, last_modified_by) FROM stdin;
    public          postgres    false    218            �          0    16404    tenants 
   TABLE DATA           k   COPY public.tenants (id, created_date, last_modified_date, created_by, last_modified_by, name) FROM stdin;
    public          postgres    false    220            �          0    16412    token 
   TABLE DATA           @   COPY public.token (expiry_date, user_id, id, token) FROM stdin;
    public          postgres    false    221            �          0    16420    users 
   TABLE DATA           e   COPY public.users (enabled, role_role_id, tenant_id, user_id, username, email, password) FROM stdin;
    public          postgres    false    223            �           0    0    bookmark_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.bookmark_id_seq', 4, true);
          public          postgres    false    215            �           0    0    refreshtoken_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.refreshtoken_seq', 51, true);
          public          postgres    false    228            �           0    0    roles_role_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.roles_role_id_seq', 1, false);
          public          postgres    false    226            �           0    0 
   tag_id_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.tag_id_seq', 8, true);
          public          postgres    false    217            �           0    0    tenants_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.tenants_id_seq', 2, true);
          public          postgres    false    219            �           0    0    tenants_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.tenants_seq', 1, false);
          public          postgres    false    229            �           0    0 	   token_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.token_seq', 1, false);
          public          postgres    false    230            �           0    0    users_user_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.users_user_id_seq', 2, true);
          public          postgres    false    222            �           2606    16393    bookmark bookmark_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.bookmark
    ADD CONSTRAINT bookmark_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.bookmark DROP CONSTRAINT bookmark_pkey;
       public            postgres    false    216            �           2606    16434    bookmark_tag bookmark_tag_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public.bookmark_tag
    ADD CONSTRAINT bookmark_tag_pkey PRIMARY KEY (bookmark_id, tag_id);
 H   ALTER TABLE ONLY public.bookmark_tag DROP CONSTRAINT bookmark_tag_pkey;
       public            postgres    false    224    224            �           2606    16439    refreshtoken refreshtoken_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.refreshtoken
    ADD CONSTRAINT refreshtoken_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.refreshtoken DROP CONSTRAINT refreshtoken_pkey;
       public            postgres    false    225            �           2606    16441 #   refreshtoken refreshtoken_token_key 
   CONSTRAINT     _   ALTER TABLE ONLY public.refreshtoken
    ADD CONSTRAINT refreshtoken_token_key UNIQUE (token);
 M   ALTER TABLE ONLY public.refreshtoken DROP CONSTRAINT refreshtoken_token_key;
       public            postgres    false    225            �           2606    16449    roles roles_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);
 :   ALTER TABLE ONLY public.roles DROP CONSTRAINT roles_pkey;
       public            postgres    false    227            �           2606    16402    tag tag_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.tag DROP CONSTRAINT tag_pkey;
       public            postgres    false    218            �           2606    16411    tenants tenants_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.tenants
    ADD CONSTRAINT tenants_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.tenants DROP CONSTRAINT tenants_pkey;
       public            postgres    false    220            �           2606    16416    token token_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.token DROP CONSTRAINT token_pkey;
       public            postgres    false    221            �           2606    16418    token token_user_id_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_user_id_key UNIQUE (user_id);
 A   ALTER TABLE ONLY public.token DROP CONSTRAINT token_user_id_key;
       public            postgres    false    221            �           2606    16429    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public            postgres    false    223            �           2606    16425    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    223            �           2606    16427    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public            postgres    false    223            �           2606    16463 (   refreshtoken fka652xrdji49m4isx38pp4p80p    FK CONSTRAINT     �   ALTER TABLE ONLY public.refreshtoken
    ADD CONSTRAINT fka652xrdji49m4isx38pp4p80p FOREIGN KEY (user_id) REFERENCES public.users(user_id);
 R   ALTER TABLE ONLY public.refreshtoken DROP CONSTRAINT fka652xrdji49m4isx38pp4p80p;
       public          postgres    false    225    3300    223            �           2606    16453 (   bookmark_tag fkhq7j2vott6kem0g51hhgq5nfl    FK CONSTRAINT     �   ALTER TABLE ONLY public.bookmark_tag
    ADD CONSTRAINT fkhq7j2vott6kem0g51hhgq5nfl FOREIGN KEY (tag_id) REFERENCES public.tag(id);
 R   ALTER TABLE ONLY public.bookmark_tag DROP CONSTRAINT fkhq7j2vott6kem0g51hhgq5nfl;
       public          postgres    false    3290    224    218            �           2606    16468 !   token fkj8rfw4x0wjjyibfqq566j4qng    FK CONSTRAINT     �   ALTER TABLE ONLY public.token
    ADD CONSTRAINT fkj8rfw4x0wjjyibfqq566j4qng FOREIGN KEY (user_id) REFERENCES public.users(user_id);
 K   ALTER TABLE ONLY public.token DROP CONSTRAINT fkj8rfw4x0wjjyibfqq566j4qng;
       public          postgres    false    223    221    3300            �           2606    16458 (   bookmark_tag fkpfa5mq9fkkjmv9jmu4hk9igpw    FK CONSTRAINT     �   ALTER TABLE ONLY public.bookmark_tag
    ADD CONSTRAINT fkpfa5mq9fkkjmv9jmu4hk9igpw FOREIGN KEY (bookmark_id) REFERENCES public.bookmark(id);
 R   ALTER TABLE ONLY public.bookmark_tag DROP CONSTRAINT fkpfa5mq9fkkjmv9jmu4hk9igpw;
       public          postgres    false    216    224    3288            �           2606    16473 !   users fkruo12mi6hchjfi06jhln9tdkt    FK CONSTRAINT     �   ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkruo12mi6hchjfi06jhln9tdkt FOREIGN KEY (role_role_id) REFERENCES public.roles(role_id);
 K   ALTER TABLE ONLY public.users DROP CONSTRAINT fkruo12mi6hchjfi06jhln9tdkt;
       public          postgres    false    227    3310    223            �   �   x���1n�0E��� -8i�tl��E�9%1��X$9Fn_I	:� ȿ��7��M_�R�Ǒ(���䉌T'�ƜCzW*-����.�?
b��h�m�O���ْg_���CW:��(7�- T���dp,�<B��]���
����Ż�kwa����qj��mIi���+ޕy �z|~ �a������V�o]&�kϝ�[��      �      x�3�4�2�4�2�F�F\&��\1z\\\ '�      �   l   x�e̱!��H����7�5��#DW�~ғ��F{q\�_���\�8��4R�:�&0H�f��\���ha�x}��C{�!8��I�ӑ�N�w��� �       �      x�3���q�v����� %��      �   j   x�3���4���ٙy� f�X�D��&ŧ��!������Ԣ�bdq���[�f�)�(.(
+��'��1�ځ,��l����`��9��%P�=... ��4Z      �   >   x�3�4202�50�54V0��24�24�3�4�)�U��Y��Jq�lLQqb^J~:����� �?$      �      x������ � �      �   u   x�+�4�4¬��̒(吞��������˩b��bh�R��[�^�iPV�fjXlQPQbi�Ul�\^�n^�S��f\�m�Yf�i���c��e�U4����R�`4�̏���� ��=[     