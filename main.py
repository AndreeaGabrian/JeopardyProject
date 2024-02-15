from openai import OpenAI

import re
import json


def parse_text_to_json(input_file, output_file):
    questions = []
    with open(input_file, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        i = 0
        while i < len(lines):
            if lines[i].startswith("Clue:"):
                clue = lines[i][6:].strip()
                category_line = lines[i + 1].strip()
                category_match = re.search(r'(.+) \| Correct answer: (.+)', category_line)
                if category_match:
                    category = category_match.group(1).strip()
                    correct_answer = category_match.group(2).strip()
                else:
                    category = "Unknown"
                    correct_answer = "Unknown"
                top10 = [line.split(",")[0].strip()[9:] for line in lines[i + 3:i + 13]]  # Extract top 10 titles
                questions.append({
                    "clue": clue,
                    "category": category,
                    "correct_answer": correct_answer,
                    "top10": top10
                })
                i += 14  # Skip Total Results and Document lines
            else:
                i += 1

    with open(output_file, 'w') as f:
        json.dump(questions, f, indent=4)


# parse_text_to_json("results.txt", "output.json")

client = OpenAI(api_key="your api key here")


def load_data_from_json(json_file):
    with open(json_file, 'r') as f:
        data = json.load(f)
    return data[20:30]


def call_gpt(content):
    content = "; ".join(content)
    messages = [
        {"role": "user",
         "content": "You will receive 10 clues each with a category and 10 possible answers. "
                    "If you think that the first answer is wrong, rerank this 10 answers based on your knowledge "
                    "(correct answer to be the first one)." + content}
    ]

    response = client.chat.completions.create(
        messages=messages,
        model="gpt-3.5-turbo",
    )
    choices = response.choices
    completion_message = choices[0].message
    content = completion_message.content
    print(content)
    return content


# Load data from JSON file
data = load_data_from_json("output.json")
content = []
for i, gpt_string in enumerate(data):
    content.append( f"Clue {i+1}: " + gpt_string["clue"] + f"Category {i+1}: " + gpt_string["category"] + f"possible answers {i+1}: " + ", ".join(gpt_string["top10"]))



# Call GPT function
response = call_gpt(content)


def save_response(response, txt_file):

    # Save response to a text file
    with open(txt_file, 'w') as f:
        f.write(response)

save_response(response, "20-29.txt")

