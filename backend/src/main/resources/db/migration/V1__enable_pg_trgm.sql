-- Enable the PostgreSQL pg_trgm extension for trigram matching.
-- Used for fast, fuzzy perfume name and brand search and similarity ranking.
CREATE EXTENSION IF NOT EXISTS pg_trgm;
