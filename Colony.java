/*
 *  
 * ICS4U
 * Colony class
 * Conway's Game of Life
 * 
 * */
import java.awt.*;
import java.util.*;
import java.io.*;
public class Colony
{
  private boolean grid[] [];
  public static final int TOTAL_SIZE = 550;
  public static final int BOX = 3;
  public Colony (double density)
  {
    grid = new boolean [(int) (TOTAL_SIZE / BOX)] [(int) (TOTAL_SIZE / BOX)];
    for (int row = 0 ; row < grid.length ; row++)
      for (int col = 0 ; col < grid [0].length ; col++)
      grid [row] [col] = Math.random () < density;
  }
  
  public void show (Graphics g)
  {
    for (int row = 0 ; row < grid.length ; row++)
      for (int col = 0 ; col < grid [0].length ; col++)
    {
      if (grid [row] [col]) // life
        g.setColor (Color.black);
      else
        g.setColor (Color.white);
      g.fillRect (col * BOX + 2, row * BOX + 2, BOX, BOX); // draw life form
    }
  }
  
  public boolean live(int row, int col)
  {
    int neighbours = 0;
    //check above
    if(row > 0)
    {
      if(col > 0)
      {
        if(grid[row - 1][col - 1]) // check 1 left 1 up
        {
          neighbours++;
        }
      }
      
      if(grid[row - 1][col]) //check 1 up
      {
        neighbours++;
      }
      
      if(col < grid.length - 1)
      {
        if(grid[row - 1][col + 1]) //check 1 right 1 up
        {
          neighbours++;
        }
      }
    }
    
    //check same row
    if(col > 0)
    {
      if(grid[row][col - 1]) // check 1 left
      {
        neighbours++;
      }
    }
    
    if(col < grid.length - 1)
    {
      if(grid[row][col + 1]) //check 1 right
      {
        neighbours++;
      }
    }
    
    //check row below
    if(row < grid.length - 1)
    {
      
      if(col > 0)
      {
        if(grid[row + 1][col - 1]) // check 1 left 1 down
        {
          neighbours++;
        }
      }
      
      if(grid[row + 1][col]) //check 1 down
      {
        neighbours++;
      }
      
      if(col < grid.length - 1)
      {
        if(grid[row + 1][col + 1]) //check 1 right 1 down
        {
          neighbours++;
        }
      }
    }
    
    //if currently live
    if(grid[row][col])
    {
      if(neighbours < 2 || neighbours > 3)
      {
        return false;
      }
      return true;
    }
    
    //if currently dead
    if(neighbours == 3)
    {
      return true;
    }
    
    return false;
  }
  
  public void advance ()
  {
    boolean[][] temp = new boolean[grid.length][grid[0].length];
    for(int i = 0; i < temp.length; i++)
    {
      for(int j = 0; j < temp[i].length; j++)
      {
        temp[i][j] = live(i, j);
      }
    }
    grid = temp;
  }
  
  public void populate(int row, int col, int size)
  {
    for(int i = 0; i < size / 2; i++)
    {
      for(int j = 0; j < size / 2; j++)
      {
        boolean inside = false;
        if(Math.pow(i, 2) + Math.pow(j, 2) <= Math.pow(size / 2, 2))
        {
          if(Math.random() < 0.9)
          {
            inside = true;
          }
        }
        if(inside)
        {
          if(row - i > 0)
          {
            if(col - j > 0)
            {
              grid[row - i][col - j] = true;
            }
            if(col + j < grid[0].length)
            {
              grid[row - i][col + j] = true;
            }
          }
          
          if(row + i < grid.length)
          {
            if(col - j > 0)
            {
              grid[row + i][col - j] = true;
            }
            if(col + j < grid[0].length)
            {
              grid[row + i][col + j] = true;
            }
          }
        }
      }
    }
  }
  
  public void eradicate(int row, int col, int size)
  {
    for(int i = 0; i < size / 2; i++)
    {
      for(int j = 0; j < size / 2; j++)
      {
        boolean inside = false;
        if(Math.pow(i, 2) + Math.pow(j, 2) <= Math.pow(size / 2, 2))
        {
          if(Math.random() < 0.9)
          {
            inside = true;
          }
        }
        if(inside)
        {
          if(row - i > 0)
          {
            if(col - j > 0)
            {
              grid[row - i][col - j] = false;
            }
            if(col + j < grid[0].length)
            {
              grid[row - i][col + j] = false;
            }
          }
          
          if(row + i < grid.length)
          {
            if(col - j > 0)
            {
              grid[row + i][col - j] = false;
            }
            if(col + j < grid[0].length)
            {
              grid[row + i][col + j] = false;
            }
          }
        }
      }
    }
  }
  
  public void load(File file)
  {
    try {
      Scanner sc = new Scanner(new FileReader(file));
      boolean[][] temp = new boolean[(int)(TOTAL_SIZE / BOX)][(int)(TOTAL_SIZE / BOX)];
      int counter = 0;
      while (sc.hasNext() && counter < temp.length) {
        String text = sc.nextLine();
        for(int i = 0; i < temp[0].length; i++)
        {
          boolean value = false;
          try{
            if(text.charAt(i) == '0')
            {
              value = false;
            }else if(text.charAt(i) == '1')
            {
              value = true;
            }
          }catch(Exception e)
          {
            value = false;
          }
          temp[counter][i] = value;
        }
        counter++;
      }
      grid = temp;
      sc.close();
    } catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public boolean save(String name)
  {
    try{
      PrintWriter printer = new PrintWriter (new FileWriter (name));
      printer.println(grid.length);
      printer.println(grid[0].length);
      for (int i = 0; i < grid.length ; i++)
      {
        for(int j = 0; j < grid[0].length; j++)
        {
          if(grid[i][j])
          {
            printer.print("1");
          }else{
            printer.print("0");
          }
        }
        printer.println();
      }
      printer.close (); // close file
      return true;
    }catch(Exception ex)
    {
      return false;
    }
  }
}