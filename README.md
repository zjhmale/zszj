## progress

* home(_there is a ajax module should be finish after done with materials_)
* articles
* article_types
* materials
* softwares(_just left download static file with md5 hash_)
* zhongjies
* zhaobiaos
* zjshis
* gaisuans(_TODO_)
* man_markets
* man_costs
* books
* equipments
* gczjzbs -> tezbs
* session_cookie(_TODO_)
* deploy with nginx(_TODO_)

## for download static file

for more check `attachments_controller/send-file` function

```
curl -I http://localhost:3000/attachments/343
```

## Running

```
lein deps
lein ring server
```

## fun stuff

* clojurescript
* jayq

## issues

* how to flatten this shit

```
(apply merge (apply merge '({:a 1}) '({:b 2})) '({:c 3}))
```

for more details sea the `db.books/search-book` function

