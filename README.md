# 4IF_Prog_Reseau

Hexanome H4133

Walid Oulderra - Abdelbasset Omiri

## Chat-peer-to-peer

### Description

Chat permettant la connexion entre 2 clients, avec suavegarde de l'historique et possiblité d'afficher l'historique quand le client ne spécifie pas de target.

### Comment lancer

Se placer sur _Chat-peer-to-peer/src_

Lancer le serveur: `java stream.EchoServer 5000`

Lancer un client: `java stream.EchoClient localhost 5000`

## Group-Chat

### Description

Implementation de groupe chat globale quand un utilisateur se connecte il est mis dans le groupe et peut recevoir les messages de tous les autres utilisateurs dans le groupe

### Comment lancer

Se placer sur _Groupe-Chat/src_

Lancer le serveur: `java server.ChatServer`

Lancer un client : `java client.Client`

## HTPP-Server

Implementation d'un serveur http permettant de répondre aux requêtes HTPPs tel que GET,POST, DELETE et lecture de différents formats de média text,html,video,image...

### Comment lancer

Se placer sur _HTTP-Server/src_

Lancer le serveur: `java http.server.WebServer`

Le serveur tourne sur le port 3000 par defaut localhost:3000/
