BEGIN TRANSACTION;
CREATE TABLE "sp__process_step" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`process_id`	INTEGER NOT NULL,
	`interval_after_start`	INTEGER NOT NULL,
	`caption`	TEXT NOT NULL,
	`description`	INTEGER
);
CREATE TABLE "sp__process" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`date_created`	TEXT NOT NULL,
	`title`	TEXT NOT NULL,
	`description`	TEXT
);
CREATE TABLE "sp__note" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`process_id`	INTEGER,
	`process_step_id`	INTEGER,
	`execution_id`	INTEGER,
	`attached_file`	TEXT,
	`content`	TEXT
);
CREATE TABLE "sp__execution_step" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`execution_id`	INTEGER NOT NULL,
	`process_step_id`	INTEGER NOT NULL,
	`notified_on`	TEXT,
	`status`	INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE "sp__execution" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`process_id`	INTEGER NOT NULL,
	`date_started`	TEXT,
	`date_finished`	TEXT
);
COMMIT;
