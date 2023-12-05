from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
import pinecone
import openai
from sentence_transformers import SentenceTransformer

openai.api_key = "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"
model = SentenceTransformer('all-MiniLM-L6-v2')
pinecone.init(api_key='d6c9a9d1-e4a4-4603-a6ee-b596d10abfdd', environment='gcp-starter')
index = pinecone.Index("demo-index")


def find_match(input):
    input_em = model.encode(input).tolist()
    result = index.query(input_em, top_k=2, includeMetadata=True)
    return result['matches'][0]['metadata']['text']+"\n"+result['matches'][1]['metadata']['text']

def query_refiner(conversation, query):

    response = openai.Completion.create(
    model="text-davinci-003",
    prompt=f"Given the following user query and conversation log, formulate a question that would be the most relevant to provide the user with an answer from a knowledge base.\n\nCONVERSATION LOG: \n{conversation}\n\nQuery: {query}\n\nRefined Query:",
    temperature=0.7,
    max_tokens=256,
    top_p=1,
    frequency_penalty=0,
    presence_penalty=0
    )
    return response['choices'][0]['text']



app = FastAPI()

class ChatRequest(BaseModel):
    messages: List[dict]

class QueryRequest(BaseModel):
    query: str

class ChatResponse(BaseModel):
    response: str
    requests: List[str]

@app.post("/chat")
async def chat(chat_request: ChatRequest):
    messages = chat_request.messages

    refined_query = query_refiner(messages[0:-1], messages[-1])
    messages = messages[0:-1]
    messages.append({"role": "user", "content": refined_query})

    context = find_match(refined_query)
    messages.insert(0, {"role": "system", "content": f"Here is the vector search from the Indian Laws Database {context}. Answer the question with the given context or your knowledge. But be absolutely sure of your answer and do not hallucinate "})
    response = openai.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=messages
    )
    value = response.choices[0].message
    print(value)
    return value
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
