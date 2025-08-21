--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2025-08-21 02:06:22

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 17264)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    id integer NOT NULL,
    category_name character varying(150) NOT NULL
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17267)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_id_seq OWNER TO postgres;

--
-- TOC entry 4899 (class 0 OID 0)
-- Dependencies: 216
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- TOC entry 217 (class 1259 OID 17268)
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    id bigint NOT NULL,
    account_number character varying(250),
    address text NOT NULL,
    phone_number character varying(20) NOT NULL
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17273)
-- Name: item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item (
    id bigint NOT NULL,
    item_name character varying(255) NOT NULL,
    description text DEFAULT '--- no description ---'::text,
    stock integer DEFAULT 0 NOT NULL,
    category_id integer,
    price integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    brand character varying(255) DEFAULT '--no brand--'::character varying
);


ALTER TABLE public.item OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17282)
-- Name: item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.item_id_seq OWNER TO postgres;

--
-- TOC entry 4900 (class 0 OID 0)
-- Dependencies: 219
-- Name: item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.item_id_seq OWNED BY public.item.id;


--
-- TOC entry 220 (class 1259 OID 17283)
-- Name: sales_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_history (
    id bigint NOT NULL,
    customer_id bigint,
    grand_total integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.sales_history OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17291)
-- Name: sales_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_item (
    id bigint NOT NULL,
    sales_history_id bigint NOT NULL,
    item_id bigint,
    unit integer DEFAULT 0 NOT NULL,
    subtotal integer DEFAULT 0 NOT NULL,
    sell_price integer
);


ALTER TABLE public.sales_item OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17290)
-- Name: sell_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sell_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sell_history_id_seq OWNER TO postgres;

--
-- TOC entry 4901 (class 0 OID 0)
-- Dependencies: 221
-- Name: sell_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sell_history_id_seq OWNED BY public.sales_history.id;


--
-- TOC entry 223 (class 1259 OID 17298)
-- Name: sell_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sell_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sell_item_id_seq OWNER TO postgres;

--
-- TOC entry 4902 (class 0 OID 0)
-- Dependencies: 223
-- Name: sell_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sell_item_id_seq OWNED BY public.sales_item.id;


--
-- TOC entry 224 (class 1259 OID 17299)
-- Name: staff; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.staff (
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    is_active boolean NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE public.staff OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17302)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    name character varying(100) NOT NULL,
    id bigint NOT NULL,
    role character varying NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17307)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_seq OWNER TO postgres;

--
-- TOC entry 4903 (class 0 OID 0)
-- Dependencies: 226
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_id_seq OWNED BY public.users.id;


--
-- TOC entry 4716 (class 2604 OID 17308)
-- Name: category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- TOC entry 4717 (class 2604 OID 17309)
-- Name: item id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item ALTER COLUMN id SET DEFAULT nextval('public.item_id_seq'::regclass);


--
-- TOC entry 4722 (class 2604 OID 17310)
-- Name: sales_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_history ALTER COLUMN id SET DEFAULT nextval('public.sell_history_id_seq'::regclass);


--
-- TOC entry 4725 (class 2604 OID 17311)
-- Name: sales_item id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_item ALTER COLUMN id SET DEFAULT nextval('public.sell_item_id_seq'::regclass);


--
-- TOC entry 4728 (class 2604 OID 17312)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- TOC entry 4730 (class 2606 OID 17314)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 4732 (class 2606 OID 17316)
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- TOC entry 4734 (class 2606 OID 17318)
-- Name: item item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- TOC entry 4736 (class 2606 OID 17320)
-- Name: sales_history sell_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_history
    ADD CONSTRAINT sell_history_pkey PRIMARY KEY (id);


--
-- TOC entry 4738 (class 2606 OID 17322)
-- Name: sales_item sell_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_item
    ADD CONSTRAINT sell_item_pkey PRIMARY KEY (id);


--
-- TOC entry 4740 (class 2606 OID 17324)
-- Name: staff staff_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.staff
    ADD CONSTRAINT staff_pkey PRIMARY KEY (id);


--
-- TOC entry 4744 (class 2606 OID 17326)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 4742 (class 2606 OID 17328)
-- Name: staff username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.staff
    ADD CONSTRAINT username UNIQUE (username) INCLUDE (username);


--
-- TOC entry 4746 (class 2606 OID 17379)
-- Name: item category_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT category_id FOREIGN KEY (category_id) REFERENCES public.category(id) ON DELETE SET NULL;


--
-- TOC entry 4747 (class 2606 OID 17389)
-- Name: sales_history customer_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_history
    ADD CONSTRAINT customer_id FOREIGN KEY (customer_id) REFERENCES public.customer(id) ON DELETE SET NULL;


--
-- TOC entry 4745 (class 2606 OID 17339)
-- Name: customer id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT id FOREIGN KEY (id) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4750 (class 2606 OID 17344)
-- Name: staff id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.staff
    ADD CONSTRAINT id FOREIGN KEY (id) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4748 (class 2606 OID 17384)
-- Name: sales_item item_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_item
    ADD CONSTRAINT item_id FOREIGN KEY (item_id) REFERENCES public.item(id) ON DELETE SET NULL;


--
-- TOC entry 4749 (class 2606 OID 17354)
-- Name: sales_item sell_history_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_item
    ADD CONSTRAINT sell_history_id FOREIGN KEY (sales_history_id) REFERENCES public.sales_history(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


-- Completed on 2025-08-21 02:06:22

--
-- PostgreSQL database dump complete
--

