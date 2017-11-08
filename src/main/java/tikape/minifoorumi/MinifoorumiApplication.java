package tikape.minifoorumi;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.minifoorumi.database.*;
import tikape.minifoorumi.domain.Message;
import tikape.minifoorumi.domain.Thread;

public class MinifoorumiApplication {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:db/forum.db");
        MessageDao messageDao = new MessageDao(database);
        ThreadDao threadDao = new ThreadDao(database);

        Spark.get("/", (req, res) -> {
            List<Thread> ketjut = threadDao.findAll();
            List<Integer> koot = threadDao.findSizes();
            Map<Thread, Integer> ketjutJaKoot = new LinkedHashMap<>();
            for (int i = 0; i < ketjut.size(); i++) {
                ketjutJaKoot.put(ketjut.get(i), koot.get(i));
            }
            
            Map map = new HashMap<>();
            map.put("ketjutJaKoot", ketjutJaKoot);

            return new ThymeleafTemplateEngine().render(
                    new ModelAndView(map, "index")
            );
        });

        Spark.post("/", (req, res) -> {
            String ketjunNimi = req.queryParams("ketju");
            String aloitusviesti = req.queryParams("aloitusviesti");
            Integer ketjuId = threadDao.saveOrUpdate(new Thread(-1, ketjunNimi)).getId();
            messageDao.saveOrUpdate(new Message(-1, ketjuId, aloitusviesti, new Timestamp(System.currentTimeMillis())));
            res.redirect("/");
            return "";
        });

        Spark.get("/messages/:id", (req, res) -> {
            Integer viestiId = Integer.parseInt(req.params(":id"));
            Thread ketju = threadDao.findOne(viestiId);
            List<Message> viestit = messageDao.findOneList(viestiId);
            Map map = new HashMap<>();
            map.put("viestit", viestit);
            map.put("ketju", ketju);

            return new ThymeleafTemplateEngine().render(
                    new ModelAndView(map, "messages")
            );
        });

        Spark.post("/messages/:id", (req, res) -> {
            Integer viestiId = Integer.parseInt(req.params(":id"));
            String viesti = req.queryParams("viesti");
            messageDao.saveOrUpdate(new Message(-1, viestiId, viesti, new Timestamp(System.currentTimeMillis())));
            res.redirect("/messages/" + viestiId);
            return "";
        });
    }
}
