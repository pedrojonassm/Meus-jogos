----------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE public."user_id_auto_increment"
    INCREMENT 1
    START 1;

ALTER SEQUENCE public."user_id_auto_increment"
    OWNER TO "javaPokemon-database-user";
    
CREATE TABLE usuario
(
    id integer DEFAULT nextval('user_id_auto_increment'),
    login character varying(255),
    senha character varying(255),
    ultimo_login timestamp,
    creation_date timestamp default now(),
    email character varying(255),
    logado boolean default false,
    PRIMARY KEY (id),
    UNIQUE (login),
    UNIQUE (email)
);

ALTER TABLE public.usuario
    OWNER to "javaPokemon-database-user";

----------------------------------------------------------------------------------------------------------------------------------
    
CREATE SEQUENCE public.id_register_token_auto_increment
    INCREMENT 1
    START 1;

ALTER SEQUENCE public.id_register_token_auto_increment
    OWNER TO "javaPokemon-database-user";

CREATE TABLE public.register_token
(
    id integer DEFAULT nextval('id_register_token_auto_increment'),
    token character varying(255) NOT NULL,
    creation_date timestamp default now(),
    delete_date timestamp default now() + interval '1 day',
    redefinir_senha boolean default false,
    PRIMARY KEY (id),
    UNIQUE (token)
);

ALTER TABLE public.register_token
    OWNER to "javaPokemon-database-user";

----------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE public.id_character
    INCREMENT 1
    START 1;

ALTER SEQUENCE public.id_character
    OWNER TO "javaPokemon-database-user";

CREATE TABLE public.personagens
(
    id integer DEFAULT nextval('id_character'),
    nickname character varying(255) NOT NULL,
    conta integer default 0,
    total_capturas integer default 0,
    level integer default 0,
    exp integer default 0,
    admin_power_level integer default 0,
    online boolean default false,
    PRIMARY KEY (id),
    UNIQUE (nickname),
    FOREIGN KEY (conta)
        REFERENCES public.usuario (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE public.personagens
    OWNER to "javaPokemon-database-user";
    
----------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE public.id_bag_auto_increment
    INCREMENT 1
    START 1
    MINVALUE 1;

ALTER SEQUENCE public.id_bag_auto_increment
    OWNER TO "javaPokemon-database-user";

CREATE TABLE public.bag
(
    id integer,
    personagem integer, -- se for um inventário de personagem, é diferente de 0
    item_id integer default 0,
    PRIMARY KEY (id),
    FOREIGN KEY (personagem)
        REFERENCES public.personagens (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE public.bag
    OWNER to "javaPokemon-database-user";
   
----------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION function_criar_inventario_personagem() RETURNS TRIGGER AS $example_table$
   BEGIN
      INSERT INTO public.bag(personagem) VALUES (new.id);
      RETURN NEW;
   END;
$example_table$ LANGUAGE plpgsql;

ALTER FUNCTION public.function_criar_inventario_personagem()
    OWNER TO "javaPokemon-database-user";

CREATE TRIGGER trigger_criar_inventario_personagem AFTER INSERT ON personagens
FOR EACH ROW EXECUTE PROCEDURE function_criar_inventario_personagem();

----------------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE public.id_item_auto_increment
    INCREMENT 1
    START 1
    MINVALUE 1;

ALTER SEQUENCE public.id_item_auto_increment
    OWNER TO "javaPokemon-database-user";

CREATE TABLE public.item
(
    id integer DEFAULT nextval('id_item_auto_increment'),
    tipo integer, -- qual item é?, corresponde a posição nos sprites de itens também (x = tipo%tamanho, y = tipo/tamanho) ex: tipo = 12, então x = 2 e y = 1
    bag integer, -- se for uma bag, ele salva o id da tabela bag, caso contrário ele salva em que bag está
    quantidade integer, -- quantos tem?
    PRIMARY KEY (id),
    FOREIGN KEY (bag)
        REFERENCES public.bag (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE public.item
    OWNER to "javaPokemon-database-user";

alter table public.bag
	add FOREIGN KEY (item_id)
	    REFERENCES public.item (id) MATCH SIMPLE
	    ON UPDATE CASCADE
	    ON DELETE CASCADE
	    NOT VALID;

----------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE public.criar_bag(tipo_bag integer, bag_que_esta_dentro integer, dono_da_bag integer)
    LANGUAGE 'sql'
    
AS $BODY$
INSERT INTO public.item(tipo, bag, quantidade) values (tipo_bag, bag_que_esta_dentro, 1);

INSERT INTO public.bag(personagem, item_id) VALUES (dono_da_bag, currval('id_item_auto_increment'));
$BODY$;

ALTER PROCEDURE public.criar_bag(integer, integer, integer)
    OWNER TO "javaPokemon-database-user";
----------------------------------------------------------------------------------------------------------------------------------
    
INSERT INTO public.item(id) VALUES (0); -- necessário adicionar para criar os inventários

alter table bag alter column id set DEFAULT nextval('id_bag_auto_increment');

insert into bag(id, item_id) values (0, 0); -- representa o chão

alter table bag alter column id set DEFAULT nextval('id_bag_auto_increment');