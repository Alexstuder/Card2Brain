-- THIS SCRIPT DROP&CREATES USERS and DATABASES

DROP
user if exists 'card2BrainRUN'@'%';
DROP
user if exists 'card2BrainDBA'@'%';
flush
privileges;

DROP
database if exists CARD2BRAIN;
commit;
-- Create the Database
CREATE
database if not exists CARD2BRAIN;

-- Create the Run user
-- this is the User used in the application
CREATE
USER 'card2BrainRUN'@'%' IDENTIFIED BY 'card2BrainRun$luv2code'
    WITH MAX_QUERIES_PER_HOUR 0
        MAX_UPDATES_PER_HOUR 0
        MAX_CONNECTIONS_PER_HOUR 0
        MAX_USER_CONNECTIONS 0;
GRANT ALL privileges ON CARD2BRAIN.* TO
'card2BrainRUN'@'%';

-- This User is used to monitor the DB actions
CREATE
USER 'card2BrainDBA'@'%' IDENTIFIED BY 'card2BrainDBA$luv2code'
    WITH MAX_QUERIES_PER_HOUR 0
        MAX_UPDATES_PER_HOUR 0
        MAX_CONNECTIONS_PER_HOUR 0
        MAX_USER_CONNECTIONS 0;

GRANT ALL PRIVILEGES ON CARD2BRAIN.* TO
'card2BrainDBA'@'%';
