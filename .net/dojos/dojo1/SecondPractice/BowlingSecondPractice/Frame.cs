﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BowlingSecondPractice
{
    public class Frame
    {
        public int FirstBall { get; }
        public int SecondBall { get; }

        public Frame(int firstBall, int secondBall)
        {
            this.FirstBall = firstBall;
            this.SecondBall = secondBall;
        }

        public int Score
        {
            get { return CalculateScore(); }
        }

        public Frame NextFrame { get; set; }

        private int CalculateScore()
        {
            return FirstBall + SecondBall + Bonus();
        }

        private int Bonus()
        {
            if (FirstBall + SecondBall == 10)
            {
                return NextFrame.FirstBall;
            }
            return 0;
        }
    }
}