﻿using System;
using System.Collections.Generic;
using System.Linq;

namespace GildedRose
{
    public class Program
    {
        public IList<Item> Items;

        private static void Main(string[] args)
        {
            Console.WriteLine("OMGHAI!");

            var app = InitApp();

            app.UpdateQuality();

            Console.ReadKey();
        }

        public static Program InitApp()
        {
            var app = new Program
            {
                Items = new List<Item>
                {
                    new Item {Name = "+5 Dexterity Vest", SellIn = 10, Quality = 20},
                    new Item {Name = "Aged Brie", SellIn = 2, Quality = 0},
                    new Item {Name = "Elixir of the Mongoose", SellIn = 5, Quality = 7},
                    new Item {Name = "Sulfuras, Hand of Ragnaros", SellIn = 0, Quality = 80},
                    new Item
                    {
                        Name = "Backstage passes to a TAFKAL80ETC concert",
                        SellIn = 15,
                        Quality = 20
                    },
                    new Item {Name = "Conjured Mana Cake", SellIn = 3, Quality = 6}
                }
            };
            return app;
        }

        public void UpdateQuality()
        {
            foreach (Item item in Items.Where(IsNotLegendaryItem))
            {
                PassOneDay(item);

                Update(item);
            }
        }

        private void Update(Item item)
        {
            var updater = TryGetUpdater(item);
            if (updater!=null)
            {
                updater.Update(item);
            }
        }

        private Updater TryGetUpdater(Item item)
        {
            var suddenUpdater = new UpdateSuddenDropItemStrategy();
            var valueAddingUpdater = new ValueAddingUpdater();
            var normalUpdater = new NormalUpdater();
            var updaters = new List<Updater>() {suddenUpdater, valueAddingUpdater, normalUpdater};

            var updater = FindUpdater(updaters, item);
            return updater;
        }

        private Updater FindUpdater(List<Updater> updaters, Item item)
        {
            return updaters.FirstOrDefault(updater => updater.CanUpdate(item));
        }

        private static bool IsNotLegendaryItem(Item item)
        {
            return item.Name != "Sulfuras, Hand of Ragnaros";
        }

        private static void PassOneDay(Item item)
        {
            item.SellIn = item.SellIn - 1;
        }
    }

    public class Item
    {
        public string Name { get; set; }

        public int SellIn { get; set; }

        public int Quality { get; set; }
    }
}